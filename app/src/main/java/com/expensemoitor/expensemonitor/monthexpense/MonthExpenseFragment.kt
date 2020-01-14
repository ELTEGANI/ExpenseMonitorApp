package com.expensemoitor.expensemonitor.monthexpense

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
import com.expensemoitor.expensemonitor.R
import com.expensemoitor.expensemonitor.database.ExpenseMonitorDataBase
import com.expensemoitor.expensemonitor.databinding.MonthExpenseFragmentBinding
import com.expensemoitor.expensemonitor.myexpenses.MyExpenseFragmentDirections
import com.expensemoitor.expensemonitor.adapters.DurationsExpenseAdapter
import com.expensemoitor.expensemonitor.adapters.ExpenseListener

class MonthExpenseFragment : Fragment() {



    private lateinit var binding: MonthExpenseFragmentBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.month_expense_fragment,container,false)

        val application = requireNotNull(this.activity).application
        val dataBase = ExpenseMonitorDataBase.getInstance(application).expenseMonitorDao
        val viewModelFactory = MonthExpenseFragmentViewModelFactory(dataBase,application)

        val fragmentViewModel = ViewModelProviders.of(this,viewModelFactory)
            .get(MonthExpenseFragmentViewModel::class.java)


        binding.lifecycleOwner = this
        binding.viewModel = fragmentViewModel


        val adapter = DurationsExpenseAdapter(ExpenseListener {
            fragmentViewModel.displaySelectedExpense(it)
        })


        fragmentViewModel.navigateToSelectedExpense.observe(this, Observer {
            if (it != null){
                val direction = MyExpenseFragmentDirections.actionMyExpenseFragmentToUpdateAndDeleteExpenseFragment(it)
                findNavController().navigate(direction)
                fragmentViewModel.displaySelectedExpenseCompleted()
            }
        })



        binding.monthExpenseList.itemAnimator = DefaultItemAnimator()
        binding.monthExpenseList.adapter = adapter



        return  binding.root


    }



}
