package com.expensemoitor.expensemonitor.createnewexpense


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
import com.expensemoitor.expensemonitor.R
import com.expensemoitor.expensemonitor.databinding.CreateNewExpenseFragmentBinding
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


        binding.newdateButton.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(context,
                DatePickerDialog.OnDateSetListener {
                        view, year, monthOfYear, dayOfMonth ->
                   viewModel.currentDate.value = year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth.toString()
                },year,month,day)
            datePickerDialog.show()
        }

        viewModel.validationMsg.observe(this, Observer { isValidate->
            if (isValidate){
                Toast.makeText(context,"Please Fill Empty Field",Toast.LENGTH_LONG).show()
                viewModel.onNoEmptyFields()
            }
        })




        return binding.root
    }


}
