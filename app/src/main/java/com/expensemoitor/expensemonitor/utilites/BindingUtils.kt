package com.expensemoitor.expensemonitor.utilites

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.expensemoitor.expensemonitor.network.ExpensesResponse


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
fun bindRecyclerView(recyclerView: RecyclerView,data:List<ExpensesResponse>?){
    val adapter = recyclerView.adapter as DurationsExpenseAdapter
    adapter.submitList(data)
}

@BindingAdapter("expenseAmount")
fun TextView.setExpenseAmount(expensesResponse:ExpensesResponse?){
     expensesResponse?.let {
         text = getCurrencyFromSettings()+" "+ expenseFormat(expensesResponse.amount)
     }
}

@BindingAdapter("expenseCategory")
fun TextView.setExpensecategory(expensesResponse:ExpensesResponse?){
    expensesResponse?.let {
        text = expensesResponse.expenseCategory
    }
}

@BindingAdapter("expenseDescription")
fun TextView.setExpenseDescription(expensesResponse:ExpensesResponse?){
    expensesResponse?.let {
        text = expensesResponse.description
    }
}



@BindingAdapter("expenseDate")
fun TextView.setExpenseDate(expensesResponse: ExpensesResponse?){
    expensesResponse?.let {
        text = expensesResponse.date
    }
}






