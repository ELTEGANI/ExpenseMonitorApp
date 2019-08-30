package com.expensemoitor.expensemonitor.utilites

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.expensemoitor.expensemonitor.R
import com.google.android.material.tabs.TabLayout
import com.expensemoitor.expensemonitor.myexpenses.MyExpenseFragment
import com.expensemoitor.expensemonitor.network.GetExpensesResponse
import com.expensemoitor.expensemonitor.todayexpense.TodayExpenseAdapter


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


@BindingAdapter("handler")
fun bindViewPagerAdapter(view: ViewPager, fragment: MyExpenseFragment) {
    val adapter = fragment.fragmentManager?.let { PagerAdapter(view.context, it) }
    view.adapter = adapter
}


@BindingAdapter("pager")
fun bindViewPagerTabs(tabLayout: TabLayout, viewPager: ViewPager) {
    tabLayout.setupWithViewPager(viewPager, true)
    viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
    tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab) {
            viewPager.currentItem = tab.position
            when(tab.position){
                0->{

                }

                1->{

                }

                2->{

                }
            }
        }

        override fun onTabUnselected(tab: TabLayout.Tab) {

        }

        override fun onTabReselected(tab: TabLayout.Tab) {

        }

    })
}

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView,data:List<GetExpensesResponse>?){
    val adapter = recyclerView.adapter as TodayExpenseAdapter
    adapter.submitList(data)
}

@BindingAdapter("expenseAmount")
fun TextView.setexpenseAmount(getExpensesResponse:GetExpensesResponse?){
     getExpensesResponse?.let {
         text = getExpensesResponse.amount
     }
}



@BindingAdapter("deleteImage")
fun ImageView.setDeleteImage(getExpensesResponse: GetExpensesResponse?){
      getExpensesResponse?.let {
          setImageResource(R.drawable.ic_delete_forever)
      }
}


@BindingAdapter("editImage")
fun ImageView.setEditImage(getExpensesResponse: GetExpensesResponse?){
    getExpensesResponse?.let {
        setImageResource(R.drawable.ic_edit)
    }
}





