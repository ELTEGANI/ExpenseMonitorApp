package com.monitoryourexpenses.expenses.createnewexpense


import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.monitoryourexpenses.expenses.R
import com.monitoryourexpenses.expenses.databinding.CreateNewExpenseFragmentBinding
import java.util.*


class CreateNewExpenseFragment : Fragment() {

    private lateinit var binding: CreateNewExpenseFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.create_new_expense_fragment,container,false)

        val application = requireNotNull(this.activity).application

        val viewModelFactory = CreateNewExpenseFragmentViewModelFactory(application)

        val viewModel = ViewModelProviders.of(this,viewModelFactory)
            .get(CreateNewExpenseFragmentViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.spinner.setTitle(getString(R.string.select_or_category))
        binding.spinner.setPositiveButton(getString(R.string.close))


        binding.newDateButton.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = context?.let { it1 ->
                DatePickerDialog(
                    it1,
                    DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                        viewModel.currentDate.value = year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth.toString()
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
