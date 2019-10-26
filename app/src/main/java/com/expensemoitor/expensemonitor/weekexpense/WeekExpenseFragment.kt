package com.expensemoitor.expensemonitor.weekexpense

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DefaultItemAnimator
import com.expensemoitor.expensemonitor.R
import com.expensemoitor.expensemonitor.databinding.WeekExpenseFragmentBinding
import com.expensemoitor.expensemonitor.utilites.DurationsExpenseAdapter
import com.expensemoitor.expensemonitor.utilites.ExpenseListener
import kotlinx.android.synthetic.main.update_delete_expense_custom_dailog.view.*

class WeekExpenseFragment : Fragment() {


    private val viewModel:WeekExpenseFragmentViewModel by lazy {
        ViewModelProviders.of(this).get(WeekExpenseFragmentViewModel::class.java)
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        val binding = WeekExpenseFragmentBinding.inflate(inflater)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val adapter = DurationsExpenseAdapter(ExpenseListener {

            val dialogBuilder = context?.let { it1 -> AlertDialog.Builder(it1).create() }
            val dailogBinding = DataBindingUtil
                .inflate<ViewDataBinding>(
                    LayoutInflater.from(context), R.layout.update_delete_expense_custom_dailog,
                    null, false
                )


            val expenseAmount = dailogBinding.root.amount_editText
            val expenseDescription = dailogBinding.root.description_editText
            val category = dailogBinding.root.category_spinner

            expenseAmount.setText(it.amount)
            expenseDescription.setText(it.description)

            val updateExpense = dailogBinding.root.update_button
            val deleteExpense = dailogBinding.root.delete_button
            val cancelExpense = dailogBinding.root.cancel_button

            category.setSelection(
                (category.adapter as ArrayAdapter<String>).getPosition(
                    it.expenseCategory
                )
            )

            updateExpense.setOnClickListener {

            }

            deleteExpense.setOnClickListener {
                val builder = context?.let { it1 -> AlertDialog.Builder(it1) }
                builder?.setTitle("Delete Expense")
                builder?.setMessage("Are You Sure You Want To Delete This Expense?")
                builder?.setPositiveButton("YES") { _, which ->

                }

                builder?.setNeutralButton("Cancel") { _, _ ->

                }
                val dialog: AlertDialog? = builder?.create()
                dialog?.show()
            }

            cancelExpense.setOnClickListener {
                dialogBuilder?.dismiss()
            }

            dialogBuilder?.setView(dailogBinding.root)
            dialogBuilder?.show()
        })


        binding.weekExpenseList.itemAnimator = DefaultItemAnimator()
        binding.weekExpenseList.adapter = adapter


        return  binding.root

    }



}
