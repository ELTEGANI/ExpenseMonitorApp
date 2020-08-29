package com.monitoryourexpenses.expenses.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.monitoryourexpenses.expenses.database.Expenses
import com.monitoryourexpenses.expenses.databinding.ExpenseViewItemBinding

class DurationsExpenseAdapter(private val expenseCategoryListener: ExpenseCategoryListener) : ListAdapter<Expenses, DurationsExpenseAdapter.ViewHolder>(
    GetExpensesResponseDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.bind(getItem(position)!!, expenseCategoryListener)
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

class ExpenseCategoryListener(val onClickListener: (expense: Expenses) -> Unit) {
    fun onClick(expense: Expenses) = onClickListener(expense)
}
