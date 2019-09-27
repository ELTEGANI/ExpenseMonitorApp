package com.expensemoitor.expensemonitor.loginuser


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.expensemoitor.expensemonitor.R
import com.expensemoitor.expensemonitor.databinding.LoginUserFragmentBinding

class LoginUserFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {

        val binding : LoginUserFragmentBinding = DataBindingUtil.
            inflate(inflater,R.layout.login_user_fragment,container,false)


        return binding.root
    }



}
