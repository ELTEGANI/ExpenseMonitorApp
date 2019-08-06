package com.expensemoitor.expensemonitor.registeruser


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.expensemoitor.expensemonitor.R
import com.expensemoitor.expensemonitor.databinding.RegisterationUserFragmentBinding


class RegisterationUserFragment : Fragment() {

    private lateinit var viewModel: RegisterationUserViewModel
    private lateinit var binding: RegisterationUserFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater,R.layout.registeration_user_fragment,container,false)

        viewModel = ViewModelProviders.of(this).get(RegisterationUserViewModel::class.java)

        binding.viewModel = viewModel

        binding.lifecycleOwner = this


        viewModel.genderSelected.observe(
            this,
            Observer { genderSelected ->
            if (genderSelected.isNotEmpty()) {
                Toast.makeText(context, genderSelected, Toast.LENGTH_LONG).show()
              }
            }
        )

        return  binding.root
    }



}
