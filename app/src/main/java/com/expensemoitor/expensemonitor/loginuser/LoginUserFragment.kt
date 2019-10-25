package com.expensemoitor.expensemonitor.loginuser


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.expensemoitor.expensemonitor.R
import com.expensemoitor.expensemonitor.databinding.LoginUserFragmentBinding
import com.expensemoitor.expensemonitor.utilites.PrefManager

class LoginUserFragment : Fragment() {


    private lateinit var binding: LoginUserFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {

        if(context?.let { PrefManager.isLoggedIn(it) }!! && context?.let { PrefManager.isRegistered(it) }!!){
            findNavController().navigate(R.id.action_loginUserFragment_to_myExpenseFragment)
        }
        if (context?.let { PrefManager.isLoggedIn(it) }!! && !context?.let { PrefManager.isRegistered(it) }!!){
            findNavController().navigate(R.id.action_loginUserFragment_to_registerationUserFragment)
        }



        binding = DataBindingUtil.inflate(inflater,R.layout.login_user_fragment,container,false)

        val application = requireNotNull(this.activity).application
        val viewModelFactory = LoginUserViewModelFactory(application)
        val viewModel = ViewModelProviders.of(this,viewModelFactory)
            .get(LoginUserViewModel::class.java)



        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.loginButton.setOnClickListener {
            PrefManager.setLoggedIn(application,true)
            if (PrefManager.isRegistered(application)!!){
                findNavController().navigate(R.id.action_loginUserFragment_to_myExpenseFragment)
            }else{
                findNavController().navigate(R.id.action_loginUserFragment_to_registerationUserFragment)
            }
        }

        return binding.root
    }



}
