package com.expensemoitor.expensemonitor.utilites

import android.view.View
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter




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









