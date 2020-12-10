package com.monitoryourexpenses.expenses.usercurrency

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.monitoryourexpenses.expenses.EventObserver
import com.monitoryourexpenses.expenses.R
import com.monitoryourexpenses.expenses.databinding.GetUserCurrencyFragmentBinding
import com.monitoryourexpenses.expenses.prefs.ExpenseMonitorSharedPreferences
import com.monitoryourexpenses.expenses.utilites.setupSnackBar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class GetUserCurrencyFragment : Fragment() {

    private lateinit var getUserCurrencyFragmentBinding : GetUserCurrencyFragmentBinding
    private val getUserCurrencyUserViewModel:GetUserCurrencyUserViewModel by viewModels()
    @Inject
    lateinit var expenseMonitorSharedPreferences:ExpenseMonitorSharedPreferences


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        if(expenseMonitorSharedPreferences.hasCurrency()){
              findNavController().navigate(R.id.action_registeration_to_myExpense)
        }

        getUserCurrencyFragmentBinding = DataBindingUtil.inflate(inflater,
            R.layout.get_user_currency_fragment, container, false)

        getUserCurrencyFragmentBinding.lifecycleOwner = this
        getUserCurrencyFragmentBinding.viewModel = getUserCurrencyUserViewModel

        getUserCurrencyFragmentBinding.spinner.setTitle(getString(R.string.search_select_currency))
        getUserCurrencyFragmentBinding.spinner.setPositiveButton(getString(R.string.close))

        getUserCurrencyFragmentBinding.nextButton.setOnClickListener {
            getUserCurrencyUserViewModel.saveUserCurrency()
        }
        return getUserCurrencyFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSnackbar()
        setupNavigation()
    }

    private fun setupSnackbar() {
        view?.setupSnackBar(this,getUserCurrencyUserViewModel.snackbarText, Snackbar.LENGTH_SHORT)
    }

    private fun setupNavigation() {
        getUserCurrencyUserViewModel.selectCurrencyExpenseEvent.observe(viewLifecycleOwner, EventObserver {
            val navController = getUserCurrencyFragmentBinding.root.findNavController()
            navController.navigate(R.id.action_registeration_to_myExpense)
        })
    }



}
