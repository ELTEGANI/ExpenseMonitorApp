package com.monitoryourexpenses.expenses.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.monitoryourexpenses.expenses.monthexpense.MonthExpenseFragment
import com.monitoryourexpenses.expenses.todayexpense.TodayExpenseFragment
import com.monitoryourexpenses.expenses.weekexpense.WeekExpenseFragment

const val TODAY_EXPENSE_INDEX = 0
const val WEEK_EXPENSE_INDEX = 1
const val MONTH_EXPENSE_INDEX = 2

class PagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val tabFragmentsCreators: Map<Int, () -> Fragment> = mapOf(
        TODAY_EXPENSE_INDEX to {
            TodayExpenseFragment()
        },
        WEEK_EXPENSE_INDEX to {
            WeekExpenseFragment()
        },
        MONTH_EXPENSE_INDEX to {
            MonthExpenseFragment()
        }
    )

    override fun getItemCount() = tabFragmentsCreators.size

    override fun createFragment(position: Int): Fragment {
        return tabFragmentsCreators[position]?.invoke() ?: throw IndexOutOfBoundsException()
    }
}
