package com.monitoryourexpenses.expenses.weekexpense

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
import com.monitoryourexpenses.expenses.databinding.WeekExpenseFragmentBinding
import com.monitoryourexpenses.expenses.myexpenses.MyExpenseFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class WeekExpenseFragment : Fragment() {

    private lateinit var weekExpenseFragmentBinding : WeekExpenseFragmentBinding

    @Inject
    lateinit var adapter: DurationsExpenseAdapter

    private val weekExpenseFragmentViewModel:WeekExpenseFragmentViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        weekExpenseFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.week_expense_fragment, container, false)


        weekExpenseFragmentBinding.lifecycleOwner = this
        weekExpenseFragmentBinding.viewModel = weekExpenseFragmentViewModel

        adapter?.setOnClickListener(ExpenseCategoryListener {
            weekExpenseFragmentViewModel.displaySelectedExpense(it)
        })

        weekExpenseFragmentViewModel.navigateToSelectedExpense.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                val direction = MyExpenseFragmentDirections.actionMyExpenseFragmentToUpdateAndDeleteExpenseFragment(it)
                findNavController().navigate(direction)
                weekExpenseFragmentViewModel.displaySelectedExpenseCompleted()
            }
        })

        weekExpenseFragmentBinding.weekExpenseList.itemAnimator = DefaultItemAnimator()
        weekExpenseFragmentBinding.weekExpenseList.adapter = adapter

        weekExpenseFragmentViewModel.weekExpenses.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it.isNotEmpty()) {
                    adapter?.submitList(it.reversed())
                } else {
                    weekExpenseFragmentBinding.noExpensesTextView.visibility = View.VISIBLE
                }
            }
        })

        return weekExpenseFragmentBinding.root
    }
}
