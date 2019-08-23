package com.expensemoitor.expensemonitor.utilites

import android.view.View
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.expensemoitor.expensemonitor.MainActivity
import com.expensemoitor.expensemonitor.myexpenses.MyExpenseFragment


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


@BindingAdapter("bind:handler")
fun bindViewPagerAdapter(view: ViewPager,fragment: MyExpenseFragment) {
    val adapter = fragment.fragmentManager?.let { MainSectionsAdapter(view.context, it) }
    view.adapter = adapter
}

@BindingAdapter("bind:pager")
fun bindViewPagerTabs(view: TabLayout, pagerView: ViewPager) {
    view.setupWithViewPager(pagerView, true)
}





