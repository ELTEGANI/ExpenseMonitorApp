package com.monitoryourexpenses.expenses.prefs

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExpenseMonitorSharedPreferences @Inject constructor(@ApplicationContext var context: Context) {

    private val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun saveCurrentDate(currentDate: String)  = sharedPreferences.edit {
        putString("CURRENT_DATE", currentDate)
    }

    fun saveEndOfTheWeek(endOfTheWeek: String)  = sharedPreferences.edit {
        putString("END_OF_THE_WEEK", endOfTheWeek)
    }

    fun saveStartOfTheWeek(startOfTheWeek: String) = sharedPreferences.edit {
       putString("START_OF_THE_WEEK", startOfTheWeek)
    }

    fun saveEndOfTheMonth(endOfTheMonth: String) = sharedPreferences.edit {
        putString("END_OF_THE_MONTH", endOfTheMonth)
    }

    fun saveStartOfTheMonth(startOfTheMonth: String) = sharedPreferences.edit {
        putString("START_OF_THE_MONTH", startOfTheMonth)
    }

    fun setUserCurrency(hasCurrency: Boolean)= sharedPreferences.edit {
        putBoolean("IS_USER_CURRENCY", hasCurrency)
    }

    fun hasCurrency()= sharedPreferences.getBoolean("IS_USER_CURRENCY", false)


    fun saveCurrency(selectedCurrency: String) = sharedPreferences.edit {
        putString("SELECTED_CURRENCY_BY_USER", selectedCurrency)
    }

    fun saveCurrencyForSettings(selectedCurrency: String) = sharedPreferences.edit {
        putString("userCurrency", selectedCurrency)
    }

    fun saveExceededExpenseForSettings(expense: String) = sharedPreferences.edit {
        putString("exceed_expense", expense)
    }

    fun clearExpense() = sharedPreferences.edit {
        putString("exceed_expense",null)
    }

    fun getCurrentDate()= sharedPreferences.getString("CURRENT_DATE", null)


    fun getEndOfTheWeek()= sharedPreferences.getString("END_OF_THE_WEEK", null)


    fun getStartOfTheWeek()= sharedPreferences.getString("START_OF_THE_WEEK", null)


    fun getEndOfTheMonth()= sharedPreferences.getString("END_OF_THE_MONTH", null)


    fun getStartOfTheMonth()= sharedPreferences.getString("START_OF_THE_MONTH", null)


    fun getCurrency()= sharedPreferences.getString("SELECTED_CURRENCY_BY_USER", null)


    fun getExceedExpense() = sharedPreferences.getString("exceed_expense",null)


}
