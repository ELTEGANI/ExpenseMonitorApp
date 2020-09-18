package com.monitoryourexpenses.expenses.monthexpense

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import com.monitoryourexpenses.expenses.R
import com.monitoryourexpenses.expenses.adapters.DurationsExpenseAdapter
import com.monitoryourexpenses.expenses.adapters.ExpenseCategoryListener
import com.monitoryourexpenses.expenses.databinding.MonthExpenseFragmentBinding
import com.monitoryourexpenses.expenses.myexpenses.MyExpenseFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MonthExpenseFragment : Fragment() {

    @Inject
    lateinit var adapter: DurationsExpenseAdapter

    private val monthExpenseFragmentViewModel:MonthExpenseFragmentViewModel by viewModels()

    private lateinit var monthExpenseFragmentBinding : MonthExpenseFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        monthExpenseFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.month_expense_fragment, container, false)

        monthExpenseFragmentBinding.lifecycleOwner = this
        monthExpenseFragmentBinding.viewModel = monthExpenseFragmentViewModel

        adapter.setOnClickListener(ExpenseCategoryListener {
            monthExpenseFragmentViewModel.displaySelectedExpense(it)
        })

        monthExpenseFragmentViewModel.navigateToSelectedExpense.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                val direction = MyExpenseFragmentDirections.actionMyExpenseFragmentToUpdateAndDeleteExpenseFragment(it)
                findNavController().navigate(direction)
                monthExpenseFragmentViewModel.displaySelectedExpenseCompleted()
            }
        })

        monthExpenseFragmentBinding.monthExpenseList.itemAnimator = DefaultItemAnimator()
        monthExpenseFragmentBinding.monthExpenseList.adapter = adapter

        monthExpenseFragmentViewModel.monthExpenses.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it.isNotEmpty()) {
                    adapter.submitList(it.reversed())
                } else {
                    monthExpenseFragmentBinding.noExpensesTextView.visibility = View.VISIBLE
                } }
        })

        return monthExpenseFragmentBinding.root
    }
}
