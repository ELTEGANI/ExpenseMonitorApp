package com.monitoryourexpenses.expenses.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.monitoryourexpenses.expenses.database.Expenses
import com.monitoryourexpenses.expenses.databinding.ExpenseViewItemBinding
import com.monitoryourexpenses.expenses.prefs.ExpenseMonitorSharedPreferences
import com.monitoryourexpenses.expenses.utilites.UtilitesFunctions
import kotlinx.android.synthetic.main.expense_view_item.view.*
import kotlinx.android.synthetic.main.update_and_delete_expense_fragment.view.*
import javax.inject.Inject


class DurationsExpenseAdapter @Inject constructor() : ListAdapter<Expenses, DurationsExpenseAdapter.ViewHolder>(
    GetExpensesResponseDiffCallback()
) {
    var expenseCategoryListener: ExpenseCategoryListener? = null
    @Inject
    lateinit var expenseMonitorSharedPreferences: ExpenseMonitorSharedPreferences
    @Inject
    lateinit var utilitesFunctions: UtilitesFunctions
    fun setOnClickListener(expenseCategoryListener: ExpenseCategoryListener) {
        this.expenseCategoryListener = expenseCategoryListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        expenseCategoryListener?.let { holder.bind(getItem(position)!!, it) }
        holder.itemView.amount_textView.text = """${expenseMonitorSharedPreferences.getCurrency()} ${utilitesFunctions.expenseAmountFormatter(holder.itemView.amount_expense.text.toString())}"""
    }

    class ViewHolder private constructor(val binding: ExpenseViewItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(expense: Expenses, expenseCategoryListener: ExpenseCategoryListener) {
          binding.expensesProperties = expense
          binding.clickListener = expenseCategoryListener
          binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ExpenseViewItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class GetExpensesResponseDiffCallback : DiffUtil.ItemCallback<Expenses>() {
    override fun areItemsTheSame(oldItem: Expenses, newItem: Expenses): Boolean {
        return oldItem.expense_id == newItem.expense_id
    }
    override fun areContentsTheSame(oldItem: Expenses, newItem: Expenses): Boolean {
        return oldItem == newItem
    }
}


class ExpenseCategoryListener (val onClickListener: (expense: Expenses) -> Unit) {
    fun onClick(expense: Expenses) = onClickListener(expense)
}
