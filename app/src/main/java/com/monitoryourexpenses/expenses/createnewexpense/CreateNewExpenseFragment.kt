package com.monitoryourexpenses.expenses.createnewexpense


import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.preference.PreferenceManager
import com.monitoryourexpenses.expenses.R
import com.monitoryourexpenses.expenses.database.ExpenseMonitorDataBase
import com.monitoryourexpenses.expenses.databinding.CreateNewExpenseFragmentBinding
import com.monitoryourexpenses.expenses.utilites.PrefManager
import java.text.SimpleDateFormat
import java.util.*


class CreateNewExpenseFragment : Fragment() {

    private lateinit var binding: CreateNewExpenseFragmentBinding

    @SuppressLint("SimpleDateFormat")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.create_new_expense_fragment,container,false)

        val application = requireNotNull(this.activity).application
        val dataBase = ExpenseMonitorDataBase.getInstance(application).expenseMonitorDao
        val viewModelFactory = CreateNewExpenseFragmentViewModelFactory(dataBase,application)

        val viewModel = ViewModelProvider(this,viewModelFactory)
            .get(CreateNewExpenseFragmentViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.spinner.setTitle(getString(R.string.select_or_category))




        binding.newDateButton.setOnClickListener {
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

        viewModel.validationMsg.observe(viewLifecycleOwner, Observer { validationMsg->
            if (validationMsg != null){
                Toast.makeText(context,validationMsg,Toast.LENGTH_LONG).show()
                viewModel.onNoEmptyFields()
            }
        })


        viewModel.responseMsg.observe(viewLifecycleOwner, Observer {
            if (it != null){
                Toast.makeText(context,it,Toast.LENGTH_LONG).show()
                val navController = binding.root.findNavController()
                navController.navigate(R.id.action_createNewExpenseFragment_to_myExpenseFragment)
                viewModel.onResponseMsgDisplayed()
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
