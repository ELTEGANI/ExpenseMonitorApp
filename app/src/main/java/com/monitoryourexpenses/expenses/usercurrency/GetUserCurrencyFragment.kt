package com.monitoryourexpenses.expenses.usercurrency

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.monitoryourexpenses.expenses.R
import com.monitoryourexpenses.expenses.databinding.GetUserCurrencyFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GetUserCurrencyFragment : Fragment() {

    private lateinit var getUserCurrencyFragmentBinding : GetUserCurrencyFragmentBinding
    private val getUserCurrencyUserViewModel:GetUserCurrencyUserViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        getUserCurrencyFragmentBinding = DataBindingUtil.inflate(inflater,
            R.layout.get_user_currency_fragment, container, false)

        getUserCurrencyFragmentBinding.lifecycleOwner = this
        getUserCurrencyFragmentBinding.viewModel = getUserCurrencyUserViewModel

        getUserCurrencyFragmentBinding.spinner.setTitle(getString(R.string.search_select_currency))
        getUserCurrencyFragmentBinding.spinner.setPositiveButton(getString(R.string.close))

        getUserCurrencyFragmentBinding.nextButton.setOnClickListener {
            getUserCurrencyUserViewModel.saveUserCurrency()
            getUserCurrencyUserViewModel.genderSelected()
        }

        getUserCurrencyUserViewModel.genderSelected.observe(viewLifecycleOwner, Observer { isSelected ->
            if (!isSelected) {
                Toast.makeText(context, getString(R.string.select_currency), Toast.LENGTH_LONG).show()
              }
        })

        getUserCurrencyUserViewModel.navigateToNextScreen.observe(viewLifecycleOwner, Observer {
             if (it) {
                 val navController = getUserCurrencyFragmentBinding.root.findNavController()
                 navController.navigate(R.id.action_registeration_to_myExpense)
                 getUserCurrencyUserViewModel.onNavigationCompleted()
             }
        })

        return getUserCurrencyFragmentBinding.root
    }
}
