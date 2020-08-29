package com.monitoryourexpenses.expenses.usercurrency

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.monitoryourexpenses.expenses.R
import com.monitoryourexpenses.expenses.database.ExpenseMonitorDataBase
import com.monitoryourexpenses.expenses.databinding.GetUserCurrencyFragmentBinding

class GetUserCurrencyFragment : Fragment() {

    private lateinit var binding: GetUserCurrencyFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.get_user_currency_fragment, container, false)

        val application = requireNotNull(this.activity).application
        val dataBase = ExpenseMonitorDataBase.getInstance(application).expenseMonitorDao
        val viewModelFactory = GetUserCurrencyViewModelFactory(dataBase, application)

        val viewModel = ViewModelProvider(this, viewModelFactory)
            .get(GetUserCurrencyUserViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.spinner.setTitle(getString(R.string.search_select_currency))
        binding.spinner.setPositiveButton(getString(R.string.close))

        binding.nextButton.setOnClickListener {
            viewModel.saveUserCurrency()
            viewModel.genderSelected()
        }

        viewModel.genderSelected.observe(viewLifecycleOwner, Observer { isSelected ->
            if (!isSelected) {
                Toast.makeText(context, getString(R.string.select_currency), Toast.LENGTH_LONG).show()
              }
        })

        viewModel.navigateToNextScreen.observe(viewLifecycleOwner, Observer {
             if (it) {
                 val navController = binding.root.findNavController()
                 navController.navigate(R.id.action_registeration_to_myExpense)
                 viewModel.onNavigationCompleted()
             }
        })

        return binding.root
    }
}
