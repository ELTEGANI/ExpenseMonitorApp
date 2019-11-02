package com.expensemoitor.expensemonitor.monthexpense

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import com.expensemoitor.expensemonitor.R
import com.expensemoitor.expensemonitor.databinding.MonthExpenseFragmentBinding
import com.expensemoitor.expensemonitor.myexpenses.MyExpenseFragmentDirections
import com.expensemoitor.expensemonitor.utilites.DurationsExpenseAdapter
import com.expensemoitor.expensemonitor.utilites.ExpenseListener

class MonthExpenseFragment : Fragment() {



    private val viewModel:MonthExpenseViewModel by lazy {
        ViewModelProviders.of(this).get(MonthExpenseViewModel::class.java)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = MonthExpenseFragmentBinding.inflate(inflater)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val adapter = DurationsExpenseAdapter(ExpenseListener {
            viewModel.displaySelectedExpense(it)
        })


        viewModel.navigateToSelectedExpense.observe(this, Observer {
            if (it != null){
                val direction = MyExpenseFragmentDirections.actionMyExpenseFragmentToUpdateAndDeleteExpenseFragment(it)
                findNavController().navigate(direction)
                viewModel.displaySelectedExpenseCompleted()
            }
        })



        binding.monthExpenseList.itemAnimator = DefaultItemAnimator()
        binding.monthExpenseList.adapter = adapter



        return  binding.root


    }



}
