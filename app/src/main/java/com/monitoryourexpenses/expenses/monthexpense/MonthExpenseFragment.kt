package com.monitoryourexpenses.expenses.monthexpense

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import com.monitoryourexpenses.expenses.R
import com.monitoryourexpenses.expenses.database.ExpenseMonitorDataBase
import com.monitoryourexpenses.expenses.databinding.MonthExpenseFragmentBinding
import com.monitoryourexpenses.expenses.myexpenses.MyExpenseFragmentDirections
import com.monitoryourexpenses.expenses.adapters.DurationsExpenseAdapter
import com.monitoryourexpenses.expenses.adapters.ExpenseListener

class MonthExpenseFragment : Fragment() {



    private lateinit var binding: MonthExpenseFragmentBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.month_expense_fragment,container,false)

        val application = requireNotNull(this.activity).application
        val dataBase = ExpenseMonitorDataBase.getInstance(application).expenseMonitorDao
        val viewModelFactory = MonthExpenseFragmentViewModelFactory(dataBase,application)

        val viewModel = ViewModelProviders.of(this,viewModelFactory)
            .get(MonthExpenseFragmentViewModel::class.java)


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

        viewModel.monthExpenses.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it.isNotEmpty()){
                    adapter.submitList(it)
                }else{
                    binding.noExpensesTextView.text = getString(R.string.no_monthly_expenses)
                }            }
        })

        return  binding.root


    }



}
