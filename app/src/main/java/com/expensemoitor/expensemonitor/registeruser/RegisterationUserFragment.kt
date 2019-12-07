package com.expensemoitor.expensemonitor.registeruser


import android.os.Build.VERSION_CODES.P
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.expensemoitor.expensemonitor.R
import com.expensemoitor.expensemonitor.databinding.RegisterationUserFragmentBinding
import com.expensemoitor.expensemonitor.utilites.PrefManager


class RegisterationUserFragment : Fragment() {

    private lateinit var binding: RegisterationUserFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment



        binding = DataBindingUtil.inflate(inflater,
            R.layout.registeration_user_fragment,container,false)

        val application = requireNotNull(this.activity).application

        val viewModelFactory = RegisterationUserViewModelFactory(application)

        val viewModel = ViewModelProviders.of(this,viewModelFactory)
            .get(RegisterationUserViewModel::class.java)




        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.nameEditText.setText(PrefManager.getName(application))
        binding.emailEditText.setText(PrefManager.getEmail(application))
        binding.nextButton.setOnClickListener {
            viewModel.registerUser(binding.nameEditText.text.toString(),binding.emailEditText.text.toString())
        }

        viewModel.genderSelected.observe(this, Observer { isSelected ->
            if (!isSelected) {
                Toast.makeText(context,"Please Select Option",Toast.LENGTH_LONG).show()
                viewModel.genderAlreadySelected()
              }
            }
        )

        viewModel.errormsg.observe(this, Observer {
                if (it != null) {
                    Toast.makeText(context,it,Toast.LENGTH_LONG).show()
                    viewModel.onErrorMsgDisplayed()
                }
            })


        viewModel.navigateToNextScreen.observe(this, Observer{
             if(it){
                 val navController = binding.root.findNavController()
                 navController.navigate(R.id.action_registeration_to_myExpense)
                 viewModel.onNavigationCompleted()
             }
        })


        return  binding.root
    }


}
