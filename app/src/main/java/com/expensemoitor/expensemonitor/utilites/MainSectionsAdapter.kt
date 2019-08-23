package com.expensemoitor.expensemonitor.utilites

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.expensemoitor.expensemonitor.R
import com.expensemoitor.expensemonitor.dailyexpense.DailyExpenseFragment
import com.expensemoitor.expensemonitor.monthlyexpense.MonthlyExpenseFragment
import com.expensemoitor.expensemonitor.weeklyexpense.WeeklyExpenseFragment


class MainSectionsAdapter(context: Context, fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val mContext: Context = context.applicationContext

    override fun getItem(position: Int): Fragment? {
        when (TABS[position]) {
            DAILY -> return DailyExpenseFragment()
            WEEKLY -> return WeeklyExpenseFragment()
            MONTHLY -> return MonthlyExpenseFragment()
        }
        return null
    }

    override fun getCount(): Int {
        return TABS.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when (TABS[position]) {
            DAILY -> return mContext.resources.getString(R.string.daily)
            WEEKLY -> return mContext.resources.getString(R.string.weekly)
            MONTHLY -> return mContext.resources.getString(R.string.monthly)
        }
        return null
    }

    companion object {
        private val DAILY = 0
        private val WEEKLY = 1
        private val MONTHLY = 2

        private val TABS = intArrayOf(DAILY,WEEKLY,MONTHLY)
    }
}
