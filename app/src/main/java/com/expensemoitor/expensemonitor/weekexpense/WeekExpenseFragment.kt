package com.expensemoitor.expensemonitor.weekexpense

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import com.expensemoitor.expensemonitor.databinding.WeekExpenseFragmentBinding
import com.expensemoitor.expensemonitor.utilites.DurationsExpenseAdapter

class WeekExpenseFragment : Fragment() {


    private val viewModel:WeekExpenseFragmentViewModel by lazy {
        ViewModelProviders.of(this).get(WeekExpenseFragmentViewModel::class.java)
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        val binding = WeekExpenseFragmentBinding.inflate(inflater)

        binding.lifecycleOwner = this


        binding.viewModel = viewModel

        val adapter = DurationsExpenseAdapter()


        binding.weekExpenseList.itemAnimator = DefaultItemAnimator()
        binding.weekExpenseList.adapter = adapter


        return  binding.root

    }



}
