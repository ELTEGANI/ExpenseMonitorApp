package com.expensemoitor.expensemonitor.utilites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.expensemoitor.expensemonitor.databinding.ExpenseViewItemBinding
import com.expensemoitor.expensemonitor.network.DurationExpenseResponse

class DurationsExpenseAdapter(val expenseListener:ExpenseListener) : ListAdapter<DurationExpenseResponse,DurationsExpenseAdapter.ViewHolder>(
    GetExpensesResponseDiffCallback()){


    override fun onCreateViewHolder(parent: ViewGroup,viewType:Int): ViewHolder {
        return ViewHolder.from(parent)
    }


    override fun onBindViewHolder(holder: ViewHolder, position:Int){
       holder.bind(getItem(position)!!,expenseListener)
    }


    class ViewHolder private constructor(val binding: ExpenseViewItemBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(
            durationExpenseResponse: DurationExpenseResponse,
            expenseListener: ExpenseListener
        ) {
          binding.expensesProperties = durationExpenseResponse
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




class GetExpensesResponseDiffCallback:DiffUtil.ItemCallback<DurationExpenseResponse>(){
    override fun areItemsTheSame(oldItem: DurationExpenseResponse, newItem: DurationExpenseResponse): Boolean {
        return oldItem.id == newItem.id
    }
    override fun areContentsTheSame(oldItem: DurationExpenseResponse, newItem: DurationExpenseResponse): Boolean {
        return oldItem == newItem
    }
}




class ExpenseListener(val onClickListener:(durationExpenseResponse:DurationExpenseResponse)->Unit){
    fun onClick(durationExpenseResponse:DurationExpenseResponse) = onClickListener(durationExpenseResponse)
}
