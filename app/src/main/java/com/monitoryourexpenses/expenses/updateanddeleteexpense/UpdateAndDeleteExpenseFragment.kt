package com.monitoryourexpenses.expenses.updateanddeleteexpense

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.preference.PreferenceManager
import com.monitoryourexpenses.expenses.R
import com.monitoryourexpenses.expenses.data.ExpensesRepository
import com.monitoryourexpenses.expenses.database.ExpenseMonitorDataBase
import com.monitoryourexpenses.expenses.databinding.UpdateAndDeleteExpenseFragmentBinding
import com.monitoryourexpenses.expenses.utilites.PrefManager
import java.text.SimpleDateFormat
import java.util.*

class UpdateAndDeleteExpenseFragment : Fragment() {


    private lateinit var binding: UpdateAndDeleteExpenseFragmentBinding


    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val application = requireNotNull(activity).application
        val dataBase = ExpenseMonitorDataBase.getInstance(application).expenseMonitorDao
        binding = DataBindingUtil.inflate(inflater,R.layout.update_and_delete_expense_fragment,container,false)


        val expenseResponse = arguments?.let {
            UpdateAndDeleteExpenseFragmentArgs.fromBundle(it).selectedExpense
        }


        val viewModelFactory = expenseResponse?.let {
            UpdateAndDeleteFragmentViewModelFactory(it,ExpensesRepository(),application,dataBase)
        }


        val viewModel = ViewModelProviders.of(this,viewModelFactory)
            .get(UpdateAndDeleteFragmentViewModel::class.java)



        binding.viewModel = viewModel
        binding.lifecycleOwner =  this


        binding.spinner.setTitle(getString(R.string.select_or_category))
        binding.spinner.setPositiveButton(getString(R.string.close))

        binding.spinner.setSelection((binding.spinner.adapter as ArrayAdapter<String>).getPosition(expenseResponse?.expenseCategory))


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
           viewModel.updateExpense(binding.uuidEditText.text.toString(),binding.amountEditText.text.toString(),
               binding.descriptionEditText.text.toString(),binding.dateEditText.text.toString(),binding.spinner.selectedItem.toString())
        }


        binding.deleteExpenseButton.setOnClickListener {
            viewModel.deleteExpense(binding.uuidEditText.text.toString())
        }

        viewModel.validationMsg.observe(viewLifecycleOwner, Observer {
            if (it != null){
                Toast.makeText(context,it,Toast.LENGTH_LONG).show()
                viewModel.onValidationErrorDisplayed()
            }
        })

        viewModel.msgError.observe(viewLifecycleOwner, Observer {
            if (it != null){
                Toast.makeText(context,it, Toast.LENGTH_LONG).show()
                val navController = binding.root.findNavController()
                navController.navigate(R.id.action_updateAndDeleteExpenseFragment_to_myExpenseFragment)
                viewModel.onMsgErrorDisplayed()
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

        return binding.root
    }
}
