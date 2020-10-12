package com.monitoryourexpenses.expenses.updateanddeleteexpense

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.transition.Slide
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialContainerTransform
import com.monitoryourexpenses.expenses.EventObserver
import com.monitoryourexpenses.expenses.R
import com.monitoryourexpenses.expenses.adapters.*
import com.monitoryourexpenses.expenses.databinding.UpdateAndDeleteExpenseFragmentBinding
import com.monitoryourexpenses.expenses.prefs.ExpenseMonitorSharedPreferences
import com.monitoryourexpenses.expenses.utilites.UtilitesFunctions
import com.monitoryourexpenses.expenses.utilites.setupSnackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.create_new_expense_fragment.*
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject


@AndroidEntryPoint
class UpdateAndDeleteExpenseFragment  : Fragment() {

    lateinit var updateAndDeleteExpenseFragmentBinding : UpdateAndDeleteExpenseFragmentBinding

    var tracker: SelectionTracker<Long>? = null

    private val updateAndDeleteFragmentViewModel:UpdateAndDeleteFragmentViewModel by viewModels()

    @Inject
    lateinit var expenseMonitorSharedPreferences : ExpenseMonitorSharedPreferences


    @Inject lateinit var adapter: ExpenseCategoryAdapter


    @ExperimentalCoroutinesApi
    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        updateAndDeleteExpenseFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.update_and_delete_expense_fragment, container, false)

        val expenseResponse = arguments?.let {
            UpdateAndDeleteExpenseFragmentArgs.fromBundle(it).selectedExpense
        }


        updateAndDeleteFragmentViewModel.description.value  = expenseResponse?.description.toString()
        updateAndDeleteFragmentViewModel.amount.value       = expenseResponse?.amount.toString()
        updateAndDeleteFragmentViewModel.currentDate.value  = expenseResponse?.date.toString()
        updateAndDeleteFragmentViewModel.expenseId.value    = expenseResponse?.expense_id.toString()
        updateAndDeleteFragmentViewModel.category.value     = expenseResponse?.expenseCategory.toString()


        updateAndDeleteExpenseFragmentBinding.lifecycleOwner = this
        updateAndDeleteExpenseFragmentBinding.viewModel = updateAndDeleteFragmentViewModel

        updateAndDeleteExpenseFragmentBinding.descriptionEditText.setText(expenseResponse?.description.toString())
        updateAndDeleteExpenseFragmentBinding.amountEditText.setText(expenseResponse?.amount.toString())
        updateAndDeleteExpenseFragmentBinding.dateEditText.setText(expenseResponse?.date.toString())

        updateAndDeleteExpenseFragmentBinding.dateButton.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = context?.let { it1 -> DatePickerDialog(
                    it1,{ view, year, monthOfYear, dayOfMonth ->
                        val calendar = Calendar.getInstance()
                        calendar.set(year, monthOfYear, dayOfMonth)
                        val format = SimpleDateFormat("yyyy-MM-dd")
                        val date = format.format(c.time)
                        updateAndDeleteFragmentViewModel.currentDate.value = date
                    }, year, month, day)
            }
            datePickerDialog?.show()
        }


        updateAndDeleteFragmentViewModel.exceedsMessage.observe(viewLifecycleOwner, {
            if (it != null) {
                val builder = context?.let { it1 -> AlertDialog.Builder(it1) }
                builder?.setTitle(getString(R.string.fixed_expense_title))
                builder?.setMessage(getString(R.string.message_body_fixed_expense) + " " + it + " " + expenseMonitorSharedPreferences.getCurrency() + " " + getString(
                    R.string.message_body_fixed_expenses))
                builder?.setPositiveButton(getString(R.string.rest_fixed_expense)) { dialog, which ->
                    val li = LayoutInflater.from(context)
                    val promptsView: View = li.inflate(R.layout.alert_dialog, null)
                    val alertDialogBuilder = context?.let { AlertDialog.Builder(it) }
                    alertDialogBuilder?.setView(promptsView)
                    val expense = promptsView.findViewById<View>(R.id.editText) as EditText
                    alertDialogBuilder?.setCancelable(false)
                        ?.setPositiveButton(getString(R.string.save)) { _, id -> // get user input and set it to result
                            updateAndDeleteFragmentViewModel.saveExceedExpense(expense.text.toString())
                        }
                        ?.setNegativeButton(getString(R.string.close)) { _, id ->
                            dialog.cancel()
                        }
                    val alertDialog = alertDialogBuilder?.create()
                    alertDialog?.show()
                }
                builder?.setNegativeButton(getString(R.string.cancel_fixed_expense)) { dialog, which ->
                    updateAndDeleteFragmentViewModel.cancelExpense()
                    dialog.dismiss()
                }
                val dialog: AlertDialog? = builder?.create()
                dialog?.show()
            }
        })

        val manager = NoPredictiveAnimationsGridLayoutManager(context, spanCount = 3)
        updateAndDeleteExpenseFragmentBinding.categoryList.layoutManager = manager

        adapter.setOnClickListener(CategoryListener {
            val expenseCategory = it.CategoryName.toString()
            if (expenseCategory.isNotEmpty()){
                updateAndDeleteFragmentViewModel.category.value = expenseCategory
            }
        })

        updateAndDeleteExpenseFragmentBinding.categoryList.adapter = adapter
        updateAndDeleteFragmentViewModel.categories.observe(viewLifecycleOwner,  {
            it?.let {
                adapter.addList(it.reversed())
            }
        })

        tracker = SelectionTracker.Builder(
            "mySelection", updateAndDeleteExpenseFragmentBinding.categoryList,
            MyItemKeyProvider(updateAndDeleteExpenseFragmentBinding.categoryList),
            MyItemDetailsLookup(updateAndDeleteExpenseFragmentBinding.categoryList),
            StorageStrategy.createLongStorage()
        ).withSelectionPredicate(SelectionPredicates.createSelectSingleAnything()).build()

        adapter.tracker = tracker

        updateAndDeleteExpenseFragmentBinding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String): Boolean {
                (updateAndDeleteExpenseFragmentBinding.categoryList.adapter as ExpenseCategoryAdapter).filter(newText)
                return true
            }
        })

        return updateAndDeleteExpenseFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSnackbar()
        setupNavigation()
    }

    private fun setupSnackbar() {
        view?.setupSnackbar(this,updateAndDeleteFragmentViewModel.snackbarText, Snackbar.LENGTH_SHORT)
    }

    private fun setupNavigation() {
        updateAndDeleteFragmentViewModel.updatedExpenseEvent.observe(viewLifecycleOwner, EventObserver {
            val navController = updateAndDeleteExpenseFragmentBinding.root.findNavController()
            navController.navigate(R.id.action_updateAndDeleteExpenseFragment_to_myExpenseFragment)
        })
    }

}
