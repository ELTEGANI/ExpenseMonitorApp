package com.expensemoitor.expensemonitor.myexpenses


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController

import com.expensemoitor.expensemonitor.R
import com.expensemoitor.expensemonitor.databinding.MyExpenseFragmentBinding

class MyExpenseFragment : Fragment() {

    private lateinit var binding: MyExpenseFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {


        binding = DataBindingUtil.inflate(inflater,R.layout.my_expense_fragment,container,false)

        val application = requireNotNull(this.activity).application

        val viewModelFactory = MyExpenseFragmentViewModelFactory(application)

        val viewModel = ViewModelProviders.of(this,viewModelFactory)
            .get(MyExpenseFragmentViewModel::class.java)

        binding.viewModel = viewModel

        binding.lifecycleOwner = this


        viewModel.navigateToMyExpense.observe(this, Observer {
            shouldNavigate->if (shouldNavigate){
            val navController = binding.root.findNavController()
            navController.navigate(R.id.action_myExpenseFragment_to_createNewExpenseFragment)
            viewModel.onNavigatedToMyExpense()
        }
        })



        return binding.root
    }


}
