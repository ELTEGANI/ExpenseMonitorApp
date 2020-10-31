package com.monitoryourexpenses.expenses.createexpense

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.view.LayoutInflater
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.transition.Slide
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialContainerTransform
import com.monitoryourexpenses.expenses.EventObserver
import com.monitoryourexpenses.expenses.R
import com.monitoryourexpenses.expenses.adapters.*
import com.monitoryourexpenses.expenses.databinding.CreateNewExpenseFragmentBinding
import com.monitoryourexpenses.expenses.prefs.ExpenseMonitorSharedPreferences
import com.monitoryourexpenses.expenses.utilites.UtilitesFunctions
import com.monitoryourexpenses.expenses.utilites.setupSnackbar
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.android.synthetic.main.create_new_expense_fragment.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@AndroidEntryPoint
class CreateNewExpenseFragment: Fragment() {

    private lateinit var createNewExpenseFragmentBinding : CreateNewExpenseFragmentBinding
    private val createNewExpenseFragmentViewModel: CreateNewExpenseFragmentViewModel by viewModels()
    @Inject
    lateinit var adapter: ExpenseCategoryAdapter
    @Inject
    lateinit var expenseMonitorSharedPreferences: ExpenseMonitorSharedPreferences

    private var tracker: SelectionTracker<Long>? = null

    @Inject
    lateinit var utilitesFunctions:UtilitesFunctions
    @ExperimentalCoroutinesApi
    @SuppressLint("SimpleDateFormat")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        createNewExpenseFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.create_new_expense_fragment, container, false)

        createNewExpenseFragmentBinding.lifecycleOwner = this
        createNewExpenseFragmentBinding.viewModel = createNewExpenseFragmentViewModel

        createNewExpenseFragmentBinding.dateButton.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = context?.let { it1 -> DatePickerDialog(
                it1,
                { view, year, monthOfYear, dayOfMonth ->
                    val calendar = Calendar.getInstance()
                    calendar.set(year, monthOfYear, dayOfMonth)
                    val format = SimpleDateFormat("yyyy-MM-dd")
                    val dateString = format.format(calendar.time)
                    createNewExpenseFragmentViewModel.currentDate.value = dateString
                }, year, month, day)
            }
            datePickerDialog?.show()
        }


        createNewExpenseFragmentViewModel.exceedsMessage.observe(viewLifecycleOwner, { it ->
            if (it != null) {
                val builder = context?.let { it1 -> AlertDialog.Builder(it1) }
                builder?.setTitle(getString(R.string.fixed_expense_title))
                builder?.setMessage(getString(R.string.message_body_fixed_expense) + " " + it + " " + expenseMonitorSharedPreferences
                    .getCurrency() + " " + getString(
                                    R.string.message_body_fixed_expenses))
                builder?.setPositiveButton(getString(R.string.rest_fixed_expense)) { dialog, which ->
                    val li = LayoutInflater.from(context)
                    val promptsView: View = li.inflate(R.layout.alert_dialog, null)
                    val alertDialogBuilder = context?.let { AlertDialog.Builder(it) }
                    alertDialogBuilder?.setView(promptsView)
                    val expense = promptsView.findViewById<View>(R.id.editText) as EditText
                    alertDialogBuilder?.setCancelable(false)
                        ?.setPositiveButton(getString(R.string.save)) { _, _ ->
                           createNewExpenseFragmentViewModel.saveExceedExpense(expense.text.toString())
                        }
                        ?.setNegativeButton(getString(R.string.close)) { _, id ->
                            dialog.cancel()
                        }
                    val alertDialog = alertDialogBuilder?.create()
                    alertDialog?.show()
                }
                builder?.setNegativeButton(getString(R.string.cancel_fixed_expense)) { dialog, which ->
                    createNewExpenseFragmentViewModel.cancelExpense()
                  dialog.dismiss()
                }

                val dialog: AlertDialog? = builder?.create()
                dialog?.show()
            }
        })

        (activity as AppCompatActivity).setSupportActionBar(createNewExpenseFragmentBinding.toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        setHasOptionsMenu(true)

        val manager = NoPredictiveAnimationsGridLayoutManager(context, spanCount = 3)
        createNewExpenseFragmentBinding.categoryList.layoutManager = manager

        adapter.setOnClickListener(CategoryListener {
            val expenseCategory = it.CategoryName.toString()
            if (expenseCategory.isNotEmpty()){
                createNewExpenseFragmentViewModel.category.value = expenseCategory
            }
        })

        createNewExpenseFragmentBinding.categoryList.adapter = adapter
        createNewExpenseFragmentViewModel.categories.observe(viewLifecycleOwner,{
            it?.let {
                adapter.addList(it.reversed())
            }
        })

        tracker = SelectionTracker.Builder(
            "mySelection", createNewExpenseFragmentBinding.categoryList,
            MyItemKeyProvider(createNewExpenseFragmentBinding.categoryList),
            MyItemDetailsLookup(createNewExpenseFragmentBinding.categoryList),
            StorageStrategy.createLongStorage()).withSelectionPredicate(SelectionPredicates.createSelectSingleAnything()).build()

        adapter.tracker = tracker

        createNewExpenseFragmentBinding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String): Boolean {
                 (createNewExpenseFragmentBinding.categoryList.adapter as ExpenseCategoryAdapter).filter(newText)
                return true
            }
        })

        return createNewExpenseFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSnackbar()
        setupNavigation()
        enterTransition = MaterialContainerTransform().apply {
            startView = requireActivity().findViewById(R.id.fab)
            endView = create_new_expense_constraint
            duration = resources.getInteger(R.integer.expense_motion_duration_large).toLong()
            scrimColor = Color.TRANSPARENT
            containerColor = utilitesFunctions.themeColor(R.attr.colorSurface)
            startContainerColor = utilitesFunctions.themeColor(R.attr.colorSurface)
            endContainerColor = utilitesFunctions.themeColor(R.attr.colorSurface)
        }
        returnTransition = Slide().apply {
            duration = resources.getInteger(R.integer.expense_motion_duration_medium).toLong()
            addTarget(R.id.myNavHostfragment)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.new_expense_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.add_new_category -> {
                addNewCategory()
                true
            }
            else -> false
        }

    private fun addNewCategory() {
        val builder = context?.let { AlertDialog.Builder(it) }
        builder?.setTitle(getString(R.string.add_new_category))
        val customLayout: View = layoutInflater.inflate(R.layout.add_new_category_dialog, null)
        builder?.setView(customLayout)
        builder?.setPositiveButton(getString(R.string.ok)) { _, _ ->
            val category: EditText = customLayout.findViewById(R.id.editTextTextNewCategory)
                createNewExpenseFragmentViewModel.addNewCategory(category.text.toString())
        }
        builder?.setNegativeButton(getString(R.string.close_categories)) { dialogInterface, _ ->
           dialogInterface.dismiss()
        }
        val dialog = builder?.create()
        dialog?.show()
    }

    private fun setupSnackbar() {
        view?.setupSnackbar(this, createNewExpenseFragmentViewModel.snackbarText, Snackbar.LENGTH_SHORT)
    }

    private fun setupNavigation() {
        createNewExpenseFragmentViewModel.createdExpenseEvent.observe(viewLifecycleOwner, EventObserver {
                val navController = createNewExpenseFragmentBinding.root.findNavController()
                navController.navigate(R.id.action_createNewExpenseFragment_to_myExpenseFragment)
        })
    }

}
