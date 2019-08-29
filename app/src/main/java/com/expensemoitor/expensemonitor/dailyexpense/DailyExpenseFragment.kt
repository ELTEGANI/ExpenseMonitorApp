package com.expensemoitor.expensemonitor.dailyexpense


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.expensemoitor.expensemonitor.databinding.DailyExpenseFragmentBinding


class DailyExpenseFragment : Fragment() {

   private val viewModel:DailyExpenseFragmentViewModel by lazy {
       ViewModelProviders.of(this).get(DailyExpenseFragmentViewModel::class.java)
   }


    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
        val binding = DailyExpenseFragmentBinding.inflate(inflater)

        binding.lifecycleOwner = this


        binding.viewModel = viewModel


        return  binding.root

    }

}
