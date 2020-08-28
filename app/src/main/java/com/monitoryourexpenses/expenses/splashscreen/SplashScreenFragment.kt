package com.monitoryourexpenses.expenses.splashscreen


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.monitoryourexpenses.expenses.R
import com.monitoryourexpenses.expenses.databinding.SplashScreenFragmentBinding
import com.monitoryourexpenses.expenses.utilites.PrefManager
import kotlinx.coroutines.*


class SplashScreenFragment : Fragment() {

    private lateinit var binding: SplashScreenFragmentBinding
    private val activityScope = CoroutineScope(Dispatchers.Main)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.splash_screen_fragment,container,false)
        val application = requireNotNull(this.activity).application

        val viewModelFactory = SplashScreenViewModelFactory(application)
        val viewModel = ViewModelProvider(this,viewModelFactory)
            .get(SplashScreenViewModel::class.java)


        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        context?.let {
            Glide.with(it)
                .load(R.mipmap.ic_launcher_round)
                .transform(CircleCrop())
                .into(binding.imageView)
        }

        activityScope.launch {
            delay(1000)
            if(context?.let { PrefManager.hasCurrency(it) }!!){
                findNavController().navigate(R.id.action_loginUserFragment_to_myExpenseFragment)
            }else{
                findNavController().navigate(R.id.action_loginUserFragment_to_registerationUserFragment)
            }
        }

        return binding.root
    }
    override fun onPause() {
        activityScope.cancel()
        super.onPause()
    }
}
