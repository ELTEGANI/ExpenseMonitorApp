package com.expensemoitor.expensemonitor.utilites

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.expensemoitor.expensemonitor.network.GetExpensesResponse


@BindingAdapter("progressStatus")
fun bindingStatus(progressBar: ProgressBar,status: progressStatus?){
    when(status){
        progressStatus.LOADING->{
           progressBar.visibility = View.VISIBLE
        }
        progressStatus.DONE->{
            progressBar.visibility = View.GONE
        }
        progressStatus.ERROR->{
            progressBar.visibility = View.GONE
        }
    }
}

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView,data:List<GetExpensesResponse>?){
    val adapter = recyclerView.adapter as DurationsExpenseAdapter
    adapter.submitList(data)
}

@BindingAdapter("expenseAmount")
fun TextView.setExpenseAmount(getExpensesResponse:GetExpensesResponse?){
     getExpensesResponse?.let {
         text = PrefManager.getCurrency(context)+" "+ expenseFormat(getExpensesResponse.amount)
     }
}

@BindingAdapter("expenseCategory")
fun TextView.setExpensecategory(getExpensesResponse:GetExpensesResponse?){
    getExpensesResponse?.let {
        text = getExpensesResponse.expenseCategory
    }
}

@BindingAdapter("expenseDescription")
fun TextView.setExpenseDescription(getExpensesResponse:GetExpensesResponse?){
    getExpensesResponse?.let {
        text = getExpensesResponse.description
    }
}







