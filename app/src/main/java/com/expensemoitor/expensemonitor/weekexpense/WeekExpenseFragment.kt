package com.expensemoitor.expensemonitor.weekexpense

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
import com.expensemoitor.expensemonitor.databinding.WeekExpenseFragmentBinding
import com.expensemoitor.expensemonitor.myexpenses.MyExpenseFragmentDirections
import com.expensemoitor.expensemonitor.utilites.DurationsExpenseAdapter
import com.expensemoitor.expensemonitor.utilites.ExpenseListener

class WeekExpenseFragment : Fragment() {


    private lateinit var binding: WeekExpenseFragmentBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.week_expense_fragment,container,false)

        val application = requireNotNull(this.activity).application
        val dataBase = ExpenseMonitorDataBase.getInstance(application).expenseMonitorDao
        val viewModelFactory = WeekExpenseFragmentViewModelFactory(dataBase,application)

        val viewModel = ViewModelProviders.of(this,viewModelFactory)
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


        return  binding.root

    }



}
