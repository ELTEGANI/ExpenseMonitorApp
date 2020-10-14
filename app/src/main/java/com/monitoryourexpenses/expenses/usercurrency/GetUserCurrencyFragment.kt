package com.monitoryourexpenses.expenses.usercurrency

import android.graphics.Color
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
import androidx.transition.Slide
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialContainerTransform
import com.monitoryourexpenses.expenses.EventObserver
import com.monitoryourexpenses.expenses.R
import com.monitoryourexpenses.expenses.databinding.GetUserCurrencyFragmentBinding
import com.monitoryourexpenses.expenses.utilites.setupSnackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.create_new_expense_fragment.*

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
        }


        return getUserCurrencyFragmentBinding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSnackbar()
        setupNavigation()
    }

    private fun setupSnackbar() {
        view?.setupSnackbar(this,getUserCurrencyUserViewModel.snackbarText, Snackbar.LENGTH_SHORT)
    }

    private fun setupNavigation() {
        getUserCurrencyUserViewModel.selectCurrencyExpenseEvent.observe(viewLifecycleOwner, EventObserver {
            val navController = getUserCurrencyFragmentBinding.root.findNavController()
            navController.navigate(R.id.action_registeration_to_myExpense)
        })
    }



}
