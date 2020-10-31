package com.monitoryourexpenses.expenses.todayexpense

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
import com.monitoryourexpenses.expenses.databinding.TodayExpenseFragmentBinding
import com.monitoryourexpenses.expenses.myexpenses.MyExpenseFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TodayExpenseFragment : Fragment() {

    private lateinit var todayExpenseFragmentBinding : TodayExpenseFragmentBinding
    @Inject lateinit var adapter: DurationsExpenseAdapter
    private val todayExpenseFragmentViewModel:TodayExpenseFragmentViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        todayExpenseFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.today_expense_fragment, container, false)

        todayExpenseFragmentBinding.lifecycleOwner = this
        todayExpenseFragmentBinding.viewModel = todayExpenseFragmentViewModel

        todayExpenseFragmentViewModel.navigateToSelectedExpense.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                val direction = MyExpenseFragmentDirections.actionMyExpenseFragmentToUpdateAndDeleteExpenseFragment(it)
                findNavController().navigate(direction)
                todayExpenseFragmentViewModel.displaySelectedExpenseCompleted()
            }
        })


        adapter.setOnClickListener(ExpenseCategoryListener {
           todayExpenseFragmentViewModel.displaySelectedExpense(it)
        })

        todayExpenseFragmentBinding.todayExpenseList.itemAnimator = DefaultItemAnimator()
        todayExpenseFragmentBinding.todayExpenseList.adapter = adapter

        todayExpenseFragmentViewModel.todayExpenses?.observe(viewLifecycleOwner,{
            it?.let {
                if (it.isNotEmpty()) {
                    adapter.submitList(it.reversed())
                } else {
                    todayExpenseFragmentBinding.noExpensesTextView.visibility = View.VISIBLE
                }
            }
        })

        return todayExpenseFragmentBinding.root
    }
}
