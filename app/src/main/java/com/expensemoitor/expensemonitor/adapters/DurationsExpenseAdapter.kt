package com.expensemoitor.expensemonitor.utilites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.expensemoitor.expensemonitor.databinding.ExpenseViewItemBinding
import com.expensemoitor.expensemonitor.network.GetExpensesResponse

class DurationsExpenseAdapter() : ListAdapter<GetExpensesResponse, DurationsExpenseAdapter.ViewHolder>(
    GetExpensesResponseDiffCallback()){


    override fun onCreateViewHolder(parent: ViewGroup,viewType:Int): ViewHolder {
        return ViewHolder.from(
            parent
        )
    }


    override fun onBindViewHolder(holder: ViewHolder, position:Int){
       holder.bind(getItem(position)!!)
    }


    class ViewHolder private constructor(val binding: ExpenseViewItemBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(getExpensesResponse: GetExpensesResponse) {
          binding.expensesProperties = getExpensesResponse
          binding.executePendingBindings()
        }


        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ExpenseViewItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(
                    binding
                )
            }
        }
    }
}

class GetExpensesResponseDiffCallback:DiffUtil.ItemCallback<GetExpensesResponse>(){
    override fun areItemsTheSame(oldItem: GetExpensesResponse,newItem: GetExpensesResponse): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: GetExpensesResponse,newItem: GetExpensesResponse): Boolean {
        return oldItem == newItem
    }

}




