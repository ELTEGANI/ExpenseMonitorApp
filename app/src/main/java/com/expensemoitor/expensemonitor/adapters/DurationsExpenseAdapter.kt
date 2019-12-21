package com.expensemoitor.expensemonitor.utilites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.expensemoitor.expensemonitor.databinding.ExpenseViewItemBinding
import com.expensemoitor.expensemonitor.network.ExpensesResponse

class DurationsExpenseAdapter(val expenseListener:ExpenseListener) : ListAdapter<ExpensesResponse, DurationsExpenseAdapter.ViewHolder>(
    GetExpensesResponseDiffCallback()){


    override fun onCreateViewHolder(parent: ViewGroup,viewType:Int): ViewHolder {
        return ViewHolder.from(parent)
    }


    override fun onBindViewHolder(holder: ViewHolder, position:Int){
       holder.bind(getItem(position)!!,expenseListener)
    }


    class ViewHolder private constructor(val binding: ExpenseViewItemBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(
            expensesResponse: ExpensesResponse,
            expenseListener: ExpenseListener
        ) {
          binding.expensesProperties = expensesResponse
          binding.clickListener = expenseListener
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




class GetExpensesResponseDiffCallback:DiffUtil.ItemCallback<ExpensesResponse>(){
    override fun areItemsTheSame(oldItem: ExpensesResponse, newItem: ExpensesResponse): Boolean {
        return oldItem.id == newItem.id
    }
    override fun areContentsTheSame(oldItem: ExpensesResponse, newItem: ExpensesResponse): Boolean {
        return oldItem == newItem
    }
}




class ExpenseListener(val onClickListener:(expensesResponse: ExpensesResponse)->Unit){
    fun onClick(expensesResponse: ExpensesResponse) = onClickListener(expensesResponse)
}
