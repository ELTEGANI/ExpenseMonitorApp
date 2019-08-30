package com.expensemoitor.expensemonitor.utilites

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.expensemoitor.expensemonitor.todayexpense.TodayExpenseFragment
import com.expensemoitor.expensemonitor.monthexpense.MonthlyExpenseFragment
import com.expensemoitor.expensemonitor.weekexpense.WeeklyExpenseFragment


class PagerAdapter(context: Context, fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val mContext: Context = context.applicationContext

    override fun getItem(position: Int): Fragment? {
        when (TABS[position]) {
            TODAY -> return TodayExpenseFragment()
            WEEK -> return WeeklyExpenseFragment()
            MONTH -> return MonthlyExpenseFragment()
        }
        return null
    }

    override fun getCount(): Int {
        return TABS.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when (TABS[position]) {
            TODAY -> return mContext.resources.getString(com.expensemoitor.expensemonitor.R.string.today)
            WEEK -> return "Week"
            MONTH -> return "Month"
        }
        return null
    }

    companion object {
        private val TODAY = 0
        private val WEEK = 1
        private val MONTH = 2

        private val TABS = intArrayOf(TODAY, WEEK,MONTH)
    }
}
