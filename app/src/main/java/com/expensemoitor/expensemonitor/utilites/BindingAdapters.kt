package com.expensemoitor.expensemonitor.utilites

import android.view.View
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import android.app.DatePickerDialog
import android.content.Context
import android.widget.Button
import android.widget.TextView
import androidx.databinding.ObservableField
import java.util.*




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


@BindingAdapter("selectedDate")
fun bindDateClicks(button: Button,observableField: ObservableField<String>) {
    button.setOnClickListener {
        selectDate(button.context,observableField)
    }
}



fun selectDate(context: Context,observableField:ObservableField<String>) {
    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)
    val datePickerDialog = DatePickerDialog(context,
        DatePickerDialog.OnDateSetListener {
                view, year, monthOfYear, dayOfMonth ->
            observableField.set(year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth.toString())
        },year,month,day)
    datePickerDialog.show()
}






