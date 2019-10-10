package com.monitoryourexpenses.expenses.createnewexpense


import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.monitoryourexpenses.expenses.R
import com.monitoryourexpenses.expenses.database.ExpenseMonitorDataBase
import com.monitoryourexpenses.expenses.databinding.CreateNewExpenseFragmentBinding
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

        viewModel.validationMsg.observe(this, Observer { validationMsg->
            if (validationMsg != null){
                Toast.makeText(context,validationMsg,Toast.LENGTH_LONG).show()
                viewModel.onNoEmptyFields()
            }
        })


        viewModel.responseMsg.observe(this, Observer {
            if (it != null){
                Toast.makeText(context,it,Toast.LENGTH_LONG).show()
                val navController = binding.root.findNavController()
                navController.navigate(R.id.action_createNewExpenseFragment_to_myExpenseFragment)
                viewModel.onResponseMsgDisplayed()
            }
        })
        return binding.root
    }


}
