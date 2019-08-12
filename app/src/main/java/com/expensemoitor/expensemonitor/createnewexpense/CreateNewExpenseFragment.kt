package com.expensemoitor.expensemonitor.createnewexpense


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



        viewModel.validationMsg.observe(this, Observer { isValidate->
            if (isValidate){
                Toast.makeText(context,"Please Fill Empty Field",Toast.LENGTH_LONG).show()
                viewModel.onNoEmptyFields()
            }
        })


        return binding.root
    }


}
