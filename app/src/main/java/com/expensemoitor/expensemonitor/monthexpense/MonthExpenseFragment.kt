package com.expensemoitor.expensemonitor.monthexpense

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import com.expensemoitor.expensemonitor.databinding.MonthExpenseFragmentBinding
import com.expensemoitor.expensemonitor.utilites.DurationsExpenseAdapter

class MonthExpenseFragment : Fragment() {



    private val viewModel:MonthExpenseViewModel by lazy {
        ViewModelProviders.of(this).get(MonthExpenseViewModel::class.java)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = MonthExpenseFragmentBinding.inflate(inflater)

        binding.lifecycleOwner = this


        binding.viewModel = viewModel

        val adapter = DurationsExpenseAdapter()


        binding.monthExpenseList.itemAnimator = DefaultItemAnimator()
        binding.monthExpenseList.adapter = adapter



        return  binding.root


    }



}
