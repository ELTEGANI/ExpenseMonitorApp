package com.monitoryourexpenses.expenses.monthexpense

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import com.monitoryourexpenses.expenses.R
import com.monitoryourexpenses.expenses.adapters.DurationsExpenseAdapter
import com.monitoryourexpenses.expenses.adapters.ExpenseCategoryListener
import com.monitoryourexpenses.expenses.database.ExpenseMonitorDataBase
import com.monitoryourexpenses.expenses.databinding.MonthExpenseFragmentBinding
import com.monitoryourexpenses.expenses.myexpenses.MyExpenseFragmentDirections

class MonthExpenseFragment : Fragment() {

    private lateinit var binding: MonthExpenseFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.month_expense_fragment, container, false)

        val application = requireNotNull(this.activity).application
        val dataBase = ExpenseMonitorDataBase.getInstance(application).expenseMonitorDao
        val viewModelFactory = MonthExpenseFragmentViewModelFactory(dataBase, application)

        val viewModel = ViewModelProvider(this, viewModelFactory)
            .get(MonthExpenseFragmentViewModel::class.java)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val adapter = DurationsExpenseAdapter(ExpenseCategoryListener {
            viewModel.displaySelectedExpense(it)
        })

        viewModel.navigateToSelectedExpense.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                val direction = MyExpenseFragmentDirections.actionMyExpenseFragmentToUpdateAndDeleteExpenseFragment(it)
                findNavController().navigate(direction)
                viewModel.displaySelectedExpenseCompleted()
            }
        })

        binding.monthExpenseList.itemAnimator = DefaultItemAnimator()
        binding.monthExpenseList.adapter = adapter

        viewModel.monthExpenses.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it.isNotEmpty()) {
                    adapter.submitList(it.reversed())
                } else {
                    binding.noExpensesTextView.visibility = View.VISIBLE
                } }
        })

        return binding.root
    }
}
