package com.monitoryourexpenses.expenses.usercurrency

import android.app.Application
import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.monitoryourexpenses.expenses.R
import com.monitoryourexpenses.expenses.database.Categories
import com.monitoryourexpenses.expenses.database.ExpenseMonitorDao
import com.monitoryourexpenses.expenses.database.LocalRepository
import com.monitoryourexpenses.expenses.utilites.MyApp.Companion.context
import com.monitoryourexpenses.expenses.utilites.PrefManager
import com.monitoryourexpenses.expenses.utilites.saveCurrencyForSettings
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate

class GetUserCurrencyUserViewModel(var database: ExpenseMonitorDao, var application: Application) : ViewModel() {

    private var localRepository = LocalRepository(database)
    var currency = ""

    init {
        viewModelScope.launch {
            saveAllDates()
        }
    }

    private val _navigateToNextScreen = MutableLiveData<Boolean>()
    val navigateToNextScreen: LiveData<Boolean>
        get() = _navigateToNextScreen

    private val _genderSelected = MutableLiveData<Boolean>()
    val genderSelected: LiveData<Boolean>
        get() = _genderSelected

    fun onSelectCurrencyItem(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        currency = parent.selectedItem.toString()
    }

    fun saveUserCurrency() {
       if (currency == context?.getString(R.string.select_currency)) {
           _genderSelected.value = false
       } else {
                viewModelScope.launch {
                    PrefManager.saveCurrency(application, currency.substring(range = 0..2))
                    saveCurrencyForSettings(currency)
                    PrefManager.setUserCurrency(application, true)
                    insertAllCategories()
                    _navigateToNextScreen.value = true
                }
            }
    }

    private fun saveAllDates() {
        viewModelScope.launch {
            PrefManager.saveCurrentDate(application, LocalDate.now().toString())
            PrefManager.saveStartOfTheWeek(application, LocalDate.now().toString())
            PrefManager.saveStartOfTheMonth(application, LocalDate.now().toString())
            PrefManager.saveEndOfTheWeek(application, LocalDate.now().plusDays(7).toString())
            PrefManager.saveEndOfTheMonth(application, LocalDate.now().plusMonths(1).toString())
        }
    }

    fun onNavigationCompleted() {
        _navigateToNextScreen.value = false
    }

    private suspend fun insertAllCategories() {
        val listOfCategories = listOf(
            Categories(null, context?.getString(R.string.Anniversary)),
            Categories(null, context?.getString(R.string.Adultsclothing)),
            Categories(null, context?.getString(R.string.Alimonyandchildsupport)),
            Categories(null, context?.getString(R.string.Babysitter)),
            Categories(null, context?.getString(R.string.beef)),
            Categories(null, context?.getString(R.string.Books)),
            Categories(null, context?.getString(R.string.Bigpurchases)),
            Categories(null, context?.getString(R.string.Birthday)),
            Categories(null, context?.getString(R.string.boosh)),
            Categories(null, context?.getString(R.string.juice)),
            Categories(null, context?.getString(R.string.breakfast)),
            Categories(null, context?.getString(R.string.Cable)),
            Categories(null, context?.getString(R.string.Cafes)),
            Categories(null, context?.getString(R.string.CarLeasing)),
            Categories(null, context?.getString(R.string.Carpayment)),
            Categories(null, context?.getString(R.string.Electricity)),
            Categories(null, context?.getString(R.string.Invoices)))
        localRepository.insertNewCategory(listOfCategories)
    }

    fun genderSelected() {
        _genderSelected.value = true
    }
}
