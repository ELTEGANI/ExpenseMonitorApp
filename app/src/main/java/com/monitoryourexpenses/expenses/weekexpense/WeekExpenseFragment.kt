package com.monitoryourexpenses.expenses.weekexpense

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import com.monitoryourexpenses.expenses.R
import com.monitoryourexpenses.expenses.database.ExpenseMonitorDataBase
import com.monitoryourexpenses.expenses.databinding.WeekExpenseFragmentBinding
import com.monitoryourexpenses.expenses.myexpenses.MyExpenseFragmentDirections
import com.monitoryourexpenses.expenses.adapters.DurationsExpenseAdapter
import com.monitoryourexpenses.expenses.adapters.ExpenseListener

class WeekExpenseFragment : Fragment() {


    private lateinit var binding: WeekExpenseFragmentBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.week_expense_fragment,container,false)

        val application = requireNotNull(this.activity).application
        val dataBase = ExpenseMonitorDataBase.getInstance(application).expenseMonitorDao
        val viewModelFactory = WeekExpenseFragmentViewModelFactory(dataBase,application)

        val viewModel = ViewModelProvider(this,viewModelFactory)
            .get(WeekExpenseFragmentViewModel::class.java)



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


        binding.weekExpenseList.itemAnimator = DefaultItemAnimator()
        binding.weekExpenseList.adapter = adapter

        viewModel.weekExpenses.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it.isNotEmpty()){
                    adapter.submitList(it)
                }else{
                    binding.noExpensesTextView.text = getString(R.string.no_weekly_expenses)
                }
            }
        })

        return  binding.root

    }



}
