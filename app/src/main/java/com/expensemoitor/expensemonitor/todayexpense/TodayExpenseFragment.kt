package com.expensemoitor.expensemonitor.todayexpense


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import com.expensemoitor.expensemonitor.R
import com.expensemoitor.expensemonitor.database.ExpenseMonitorDataBase
import com.expensemoitor.expensemonitor.databinding.TodayExpenseFragmentBinding
import com.expensemoitor.expensemonitor.myexpenses.MyExpenseFragmentDirections
import com.expensemoitor.expensemonitor.utilites.DurationsExpenseAdapter
import com.expensemoitor.expensemonitor.utilites.ExpenseListener


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


        binding.todayExpenseList.itemAnimator = DefaultItemAnimator()
        binding.todayExpenseList.adapter = adapter



        return  binding.root

    }

}
