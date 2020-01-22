package com.monitoryourexpenses.expenses.updateanddeleteexpense

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.monitoryourexpenses.expenses.R
import com.monitoryourexpenses.expenses.databinding.UpdateAndDeleteExpenseFragmentBinding
import java.util.*

class UpdateAndDeleteExpenseFragment : Fragment() {


    private lateinit var binding:UpdateAndDeleteExpenseFragmentBinding


    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val application = requireNotNull(activity).application
        binding = DataBindingUtil.inflate(inflater,R.layout.update_and_delete_expense_fragment,container,false)


        val expenseResponse = arguments?.let {
            UpdateAndDeleteExpenseFragmentArgs.fromBundle(it).selectedExpense
        }


        val viewModelFactory = expenseResponse?.let {
            UpdateAndDeleteFragmentViewModelFactory(it,application)
        }


        val viewModel = ViewModelProviders.of(this,viewModelFactory)
            .get(UpdateAndDeleteFragmentViewModel::class.java)


        binding.viewModel = viewModel
        binding.lifecycleOwner =  this


        binding.categorySpinner.setSelection((binding.categorySpinner.adapter as ArrayAdapter<String>).getPosition(expenseResponse?.expenseCategory))


        binding.dateButton.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = context?.let { it1 ->
                DatePickerDialog(
                    it1,
                    DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                        binding.dateEditText.setText(year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth.toString())
                    },year,month,day)
            }
            datePickerDialog?.show()
        }


        binding.updateExpenseButton.setOnClickListener {
           viewModel.updateExpense(binding.uuidEditText.text.toString(),binding.amountEditText.text.toString(),
               binding.descriptionEditText.text.toString(),binding.dateEditText.text.toString(),binding.categorySpinner.selectedItem.toString())
        }


        binding.deleteExpenseButton.setOnClickListener {
            viewModel.deleteExpense(binding.uuidEditText.text.toString())
        }

        viewModel.validationMsg.observe(this, Observer {
            if (it != null){
                Toast.makeText(context,it,Toast.LENGTH_LONG).show()
                viewModel.onValidationErrorDisplayed()
            }
        })

        viewModel.msgError.observe(this, Observer {
            if (it != null){
                Toast.makeText(context,it, Toast.LENGTH_LONG).show()
                val navController = binding.root.findNavController()
                navController.navigate(R.id.action_updateAndDeleteExpenseFragment_to_myExpenseFragment)
                viewModel.onMsgErrorDisplayed()
            }
        })

        return binding.root
    }
}
