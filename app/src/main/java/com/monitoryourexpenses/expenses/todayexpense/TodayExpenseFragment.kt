package com.monitoryourexpenses.expenses.todayexpense


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import com.monitoryourexpenses.expenses.R
import com.monitoryourexpenses.expenses.database.ExpenseMonitorDataBase
import com.monitoryourexpenses.expenses.databinding.TodayExpenseFragmentBinding
import com.monitoryourexpenses.expenses.myexpenses.MyExpenseFragmentDirections
import com.monitoryourexpenses.expenses.adapters.DurationsExpenseAdapter
import com.monitoryourexpenses.expenses.adapters.ExpenseListener


class TodayExpenseFragment : Fragment() {

    private lateinit var binding: TodayExpenseFragmentBinding

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.today_expense_fragment,container,false)

        val application = requireNotNull(this.activity).application
        val dataBase = ExpenseMonitorDataBase.getInstance(application).expenseMonitorDao
        val viewModelFactory = TodayExpenseFragmentViewModelFactory(dataBase,application)

        val viewModel = ViewModelProviders.of(this,viewModelFactory)
            .get(TodayExpenseFragmentViewModel::class.java)


        binding.lifecycleOwner = this
        binding.viewModel = viewModel


        viewModel.navigateToSelectedExpense.observe(this, Observer {
            if (it != null){
                val direction = MyExpenseFragmentDirections.actionMyExpenseFragmentToUpdateAndDeleteExpenseFragment(it)
                findNavController().navigate(direction)
                viewModel.displaySelectedExpenseCompleted()
            }
        })

        val adapter = DurationsExpenseAdapter(ExpenseListener {
            viewModel.displaySelectedExpense(it)
        })

        binding.todayExpenseList.itemAnimator = DefaultItemAnimator()
        binding.todayExpenseList.adapter = adapter

        viewModel.todayExpenses.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it.isNotEmpty()){
                    adapter.submitList(it)
                }else{
                    binding.noExpensesTextView.text = getString(R.string.no_daily_expenses)
                }
            }
        })

        return  binding.root

    }

}
