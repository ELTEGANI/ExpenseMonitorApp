package com.monitoryourexpenses.expenses.usercurrency

import android.content.Context
import android.view.View
import android.widget.AdapterView
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.monitoryourexpenses.expenses.Event
import com.monitoryourexpenses.expenses.R
import com.monitoryourexpenses.expenses.database.Categories
import com.monitoryourexpenses.expenses.database.LocalRepository
import com.monitoryourexpenses.expenses.prefs.ExpenseMonitorSharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate

class GetUserCurrencyUserViewModel @ViewModelInject
    constructor(var localRepository: LocalRepository,
                var expenseMonitorSharedPreferences: ExpenseMonitorSharedPreferences,
                @ApplicationContext var context: Context) : ViewModel() {

    var currency:String? =null

    init {
        viewModelScope.launch {
            saveAllDates()
        }
    }

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>>
        get() = _snackbarText

    private val _selectCurrencyExpenseEvent = MutableLiveData<Event<Unit>>()
    val selectCurrencyExpenseEvent: LiveData<Event<Unit>> = _selectCurrencyExpenseEvent

    fun onSelectCurrencyItem(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        currency = parent.selectedItem.toString()
    }

    fun saveUserCurrency() {
       if (currency == context.getString(R.string.select_currency)) {
           _snackbarText.value = Event(R.string.select_currency)
       } else {
                viewModelScope.launch {
                    currency?.substring(range = 0..2)?.let {selectedCurrency->
                        expenseMonitorSharedPreferences.saveCurrency(selectedCurrency)
                    }
                    currency?.let { expenseMonitorSharedPreferences.saveCurrencyForSettings(it) }
                    expenseMonitorSharedPreferences.setUserCurrency(true)
                    insertAllCategories()
                    _selectCurrencyExpenseEvent.value = Event(Unit)
                }
            }
    }

    private fun saveAllDates() {
        viewModelScope.launch {
            expenseMonitorSharedPreferences.saveCurrentDate(LocalDate.now().toString())
            expenseMonitorSharedPreferences.saveStartOfTheWeek(LocalDate.now().toString())
            expenseMonitorSharedPreferences.saveStartOfTheMonth(LocalDate.now().toString())
            expenseMonitorSharedPreferences.saveEndOfTheWeek(LocalDate.now().plusDays(7).toString())
            expenseMonitorSharedPreferences.saveEndOfTheMonth(LocalDate.now().plusMonths(1).toString())
        }
    }

    private suspend fun insertAllCategories() {
        viewModelScope.launch {
            val listOfCategories = listOf(
                Categories(null, context.getString(R.string.Anniversary)),
                Categories(null, context.getString(R.string.Adultsclothing)),
                Categories(null, context.getString(R.string.Alimonyandchildsupport)),
                Categories(null, context.getString(R.string.Babysitter)),
                Categories(null, context.getString(R.string.beef)),
                Categories(null, context.getString(R.string.Books)),
                Categories(null, context.getString(R.string.Bigpurchases)),
                Categories(null, context.getString(R.string.Birthday)),
                Categories(null, context.getString(R.string.boosh)),
                Categories(null, context.getString(R.string.juice)),
                Categories(null, context.getString(R.string.breakfast)),
                Categories(null, context.getString(R.string.Cable)),
                Categories(null, context.getString(R.string.Cafes)),
                Categories(null, context.getString(R.string.CarLeasing)),
                Categories(null, context.getString(R.string.Carpayment)),
                Categories(null, context.getString(R.string.Electricity)),
                Categories(null, context.getString(R.string.Invoices))
            )
            localRepository.insertNewCategory(listOfCategories)
        }
    }
}
