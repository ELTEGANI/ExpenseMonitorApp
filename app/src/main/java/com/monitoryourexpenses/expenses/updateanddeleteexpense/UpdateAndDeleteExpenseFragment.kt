package com.monitoryourexpenses.expenses.updateanddeleteexpense

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import com.monitoryourexpenses.expenses.R
import com.monitoryourexpenses.expenses.adapters.*
import com.monitoryourexpenses.expenses.database.ExpenseMonitorDataBase
import com.monitoryourexpenses.expenses.databinding.UpdateAndDeleteExpenseFragmentBinding
import com.monitoryourexpenses.expenses.utilites.PrefManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.text.SimpleDateFormat
import java.util.*

class UpdateAndDeleteExpenseFragment : Fragment() {


    private lateinit var binding: UpdateAndDeleteExpenseFragmentBinding
    private var tracker: SelectionTracker<Long>? = null
    var category:String? = null

    private val _validationMsg = MutableLiveData<String>()
    val validationMsg: LiveData<String>
        get() = _validationMsg

    @ExperimentalCoroutinesApi
    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val application = requireNotNull(activity).application
        val dataBase = ExpenseMonitorDataBase.getInstance(application).expenseMonitorDao
        binding = DataBindingUtil.inflate(inflater,R.layout.update_and_delete_expense_fragment,container,false)


        val expenseResponse = arguments?.let {
            UpdateAndDeleteExpenseFragmentArgs.fromBundle(it).selectedExpense
        }


        val viewModelFactory = expenseResponse?.let {
            UpdateAndDeleteFragmentViewModelFactory(it,application,dataBase)
        }


        val viewModel = ViewModelProviders.of(this,viewModelFactory)
            .get(UpdateAndDeleteFragmentViewModel::class.java)



        binding.viewModel = viewModel
        binding.lifecycleOwner =  this


        binding.dateButton.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = context?.let { it1 -> DatePickerDialog(
                    it1,
                    DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                        val c = Calendar.getInstance()
                        c.set(year, monthOfYear, dayOfMonth)
                        val format = SimpleDateFormat("yyyy-MM-dd")
                        val datestring = format.format(c.time)
                        viewModel.currentDate.value = datestring
                    },year,month,day)
            }
            datePickerDialog?.show()
        }


        binding.updateExpenseButton.setOnClickListener {
           viewModel.updateExpense(expenseResponse?.expense_id.toString(),binding.amountEditText.text.toString(),
               binding.descriptionEditText.text.toString(),binding.dateEditText.text.toString(),category.toString())
        }


        binding.deleteExpenseButton.setOnClickListener {
            viewModel.deleteExpense(expenseResponse?.expense_id.toString())
        }

        viewModel.validationMsg.observe(viewLifecycleOwner, Observer {
            if (it != null){
                Toast.makeText(context,it, Toast.LENGTH_LONG).show()
                val navController = binding.root.findNavController()
                navController.navigate(R.id.action_updateAndDeleteExpenseFragment_to_myExpenseFragment)
                viewModel.onValidationErrorDisplayed()
            }
        })



        viewModel.exceedsMessage.observe(viewLifecycleOwner, Observer {
            if (it != null){
                val builder = context?.let { it1 -> AlertDialog.Builder(it1) }
                builder?.setTitle(getString(R.string.fixed_expense_title))
                builder?.setMessage(getString(R.string.message_body_fixed_expense)+" "+it+" "+ PrefManager.getCurrency(context)+" "+getString(
                    R.string.message_body_fixed_expenses))
                builder?.setPositiveButton(getString(R.string.rest_fixed_expense)){ dialog, which ->
                    val li = LayoutInflater.from(context)
                    val promptsView: View = li.inflate(R.layout.alert_dialog, null)
                    val alertDialogBuilder = context?.let { AlertDialog.Builder(it) }
                    // set alert_dialog.xml to alertdialog builder
                    alertDialogBuilder?.setView(promptsView)
                    val userInput = promptsView.findViewById<View>(R.id.editText) as EditText
                    // set dialog message
                    alertDialogBuilder?.setCancelable(false)
                        ?.setPositiveButton(getString(R.string.save)) { _, id -> // get user input and set it to result
                            if(userInput.text.toString().isNotEmpty()){
                                val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context).edit()
                                sharedPreferences.putString("exceed_expense",userInput.text.toString())
                                sharedPreferences.apply()
                            }else{
                                Toast.makeText(context,getString(R.string.enter_fixed_expense),Toast.LENGTH_LONG).show()
                            }

                        }
                        ?.setNegativeButton(getString(R.string.close)) { _, id ->
                            dialog.cancel()
                        }
                    // create alert dialog
                    val alertDialog = alertDialogBuilder?.create()
                    // show it
                    alertDialog?.show()
                }
                builder?.setNegativeButton(getString(R.string.cancel_fixed_expense)){ dialog, which ->
                    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context).edit()
                    sharedPreferences.putString("exceed_expense",null)
                    sharedPreferences.apply()
                    dialog.dismiss()
                }

                val dialog: AlertDialog? = builder?.create()
                dialog?.show()
            }
        })

        val manager = NoPredictiveAnimationsGridLayoutManager(context,spanCount = 3)
        binding.categoryList.layoutManager = manager


        val adapter = ExpenseCategoryAdapter(CategoryListener{
            category = it.CategoryName.toString()
        })

        binding.categoryList.adapter = adapter
        viewModel.categories.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.addList(it)
            }
        })

        tracker = SelectionTracker.Builder<Long>(
            "mySelection",binding.categoryList,
            MyItemKeyProvider(binding.categoryList),
            MyItemDetailsLookup(binding.categoryList),
            StorageStrategy.createLongStorage()
        ).withSelectionPredicate(SelectionPredicates.createSelectSingleAnything()).build()

        adapter.tracker = tracker

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String): Boolean {
                (binding.categoryList.adapter as ExpenseCategoryAdapter).filter(newText)
                return true
            }
        })

        return binding.root
    }
}
