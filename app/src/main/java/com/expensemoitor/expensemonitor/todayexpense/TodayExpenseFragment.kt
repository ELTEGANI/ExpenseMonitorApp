package com.expensemoitor.expensemonitor.todayexpense


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import com.expensemoitor.expensemonitor.databinding.TodayExpenseFragmentBinding
import com.expensemoitor.expensemonitor.utilites.DurationsExpenseAdapter


class TodayExpenseFragment : Fragment() {

   private val viewModel:TodayExpenseFragmentViewModel by lazy {
       ViewModelProviders.of(this).get(TodayExpenseFragmentViewModel::class.java)
   }


    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
        val binding = TodayExpenseFragmentBinding.inflate(inflater)

        binding.lifecycleOwner = this


        binding.viewModel = viewModel

        val adapter = DurationsExpenseAdapter()


        binding.todayExpenseList.itemAnimator = DefaultItemAnimator()
        binding.todayExpenseList.adapter = adapter


        viewModel.navigateToUpdateExpense.observe(this, Observer {
            if (it.isNotEmpty()){
                Toast.makeText(context,"navigate",Toast.LENGTH_LONG).show()
            }
        })



        return  binding.root

    }

}
