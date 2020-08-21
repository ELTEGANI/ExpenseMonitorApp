package com.monitoryourexpenses.expenses.createexpense


import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.*
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import com.monitoryourexpenses.expenses.R
import com.monitoryourexpenses.expenses.adapters.*
import com.monitoryourexpenses.expenses.database.Categories
import com.monitoryourexpenses.expenses.database.ExpenseMonitorDataBase
import com.monitoryourexpenses.expenses.databinding.CreateNewExpenseFragmentBinding
import com.monitoryourexpenses.expenses.utilites.PrefManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.text.SimpleDateFormat
import java.util.*


class CreateNewExpenseFragment : Fragment() {

    private lateinit var binding: CreateNewExpenseFragmentBinding
    lateinit var viewModel: CreateNewExpenseFragmentViewModel
    private var tracker: SelectionTracker<Long>? = null
    var category:String? = null

    @ExperimentalCoroutinesApi
    @SuppressLint("SimpleDateFormat")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.create_new_expense_fragment,container,false)

        val application = requireNotNull(this.activity).application
        val dataBase = ExpenseMonitorDataBase.getInstance(application).expenseMonitorDao
        val viewModelFactory = CreateNewExpenseFragmentViewModelFactory(dataBase,application)

        viewModel = ViewModelProvider(this,viewModelFactory).get(CreateNewExpenseFragmentViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this



        binding.dateButton.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = context?.let { it1 -> DatePickerDialog(
                it1,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    val calendar = Calendar.getInstance()
                    calendar.set(year, monthOfYear, dayOfMonth)
                    val format = SimpleDateFormat("yyyy-MM-dd")
                    val dateString = format.format(calendar.time)
                    viewModel.currentDate.value = dateString
                },year,month,day)
            }
            datePickerDialog?.show()
        }

        viewModel.validationMsg.observe(viewLifecycleOwner, Observer { validationMsg->
            if (validationMsg != null){
                Toast.makeText(context,validationMsg,Toast.LENGTH_LONG).show()
                val navController = binding.root.findNavController()
                navController.navigate(R.id.action_createNewExpenseFragment_to_myExpenseFragment)
            }
        })




        viewModel.exceedsMessage.observe(viewLifecycleOwner, Observer {
            if (it != null){
                val builder = context?.let { it1 -> AlertDialog.Builder(it1) }
                builder?.setTitle(getString(R.string.fixed_expense_title))
                builder?.setMessage(getString(R.string.message_body_fixed_expense)+" "+it+" "+PrefManager.getCurrency(context)+" "+getString(
                                    R.string.message_body_fixed_expenses))
                builder?.setPositiveButton(getString(R.string.rest_fixed_expense)){ dialog, which ->
                    val li = LayoutInflater.from(context)
                    val promptsView: View = li.inflate(R.layout.alert_dialog, null)
                    val alertDialogBuilder = context?.let { AlertDialog.Builder(it) }
                    alertDialogBuilder?.setView(promptsView)
                    val userInput = promptsView.findViewById<View>(R.id.editText) as EditText
                    alertDialogBuilder?.setCancelable(false)
                        ?.setPositiveButton(getString(R.string.save)) { _, _ -> // get user input and set it to result
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
                    val alertDialog = alertDialogBuilder?.create()
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

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        setHasOptionsMenu(true)

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
            StorageStrategy.createLongStorage()).withSelectionPredicate(SelectionPredicates.createSelectSingleAnything()).build()

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

        binding.createNewExpenseButton.setOnClickListener {
                viewModel.createNewExpense(binding.expenseAmountTextView.text.toString(),binding.expenseDescriptionTextView.text.toString(),
                    binding.expenseDateTextView.text.toString(),category.toString())

        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.new_expense_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.add_new_category -> {
                addNewCategory()
                true
            }
            else -> false
        }


    private fun addNewCategory(){
        val builder = context?.let { AlertDialog.Builder(it) }
        builder?.setTitle(getString(R.string.add_new_category))
        val customLayout: View = layoutInflater.inflate(R.layout.add_new_category_dialog, null)
        builder?.setView(customLayout)
        builder?.setPositiveButton(getString(R.string.ok)) { _, _ ->
            val editText: EditText = customLayout.findViewById(R.id.editTextTextNewCategory)
            viewModel.addNewCategory(Categories(id = null,CategoryName =  editText.text.toString()))
        }
        builder?.setNegativeButton(getString(R.string.close_categories)){ dialogInterface, _ ->
           dialogInterface.dismiss()
        }
        val dialog = builder?.create()
        dialog?.show()
    }


}
