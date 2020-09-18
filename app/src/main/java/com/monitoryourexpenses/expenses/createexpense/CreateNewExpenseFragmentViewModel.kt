package com.monitoryourexpenses.expenses.createexpense

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.preference.PreferenceManager
import com.monitoryourexpenses.expenses.database.Categories
import com.monitoryourexpenses.expenses.database.Expenses
import com.monitoryourexpenses.expenses.database.LocalRepository
import com.monitoryourexpenses.expenses.prefs.ExpenseMonitorSharedPreferences
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import javax.inject.Inject

class CreateNewExpenseFragmentViewModel @ViewModelInject constructor(val localRepository: LocalRepository,
                   var sharedPreferences: ExpenseMonitorSharedPreferences) : ViewModel() {
    val amount = MutableLiveData<String>()
    val description = MutableLiveData<String>()
    val currentDate = MutableLiveData<String>()
    val categories = localRepository.getAllCategories()

    init {
        currentDate.value = LocalDate.now().toString()
    }

    private val _validationMsg = MutableLiveData<Boolean>()
    val validationMsg: LiveData<Boolean>
        get() = _validationMsg

    private val _makeSelection = MutableLiveData<Boolean>()
    val makeSelection: LiveData<Boolean>
        get() = _makeSelection

    private val _exceedsMessage = MutableLiveData<String>()
    val exceedsMessage: LiveData<String>
        get() = _exceedsMessage

    fun createNewExpense(amount: String, description: String, date: String, category: String) {
        if (amount.isEmpty() || description.isEmpty()) {
            _validationMsg.value = false
        } else if (category.isEmpty()) {
            _makeSelection.value = false
        } else {
            viewModelScope.launch {
                if (localRepository.sumationOfSpecifiedExpenses(
                        sharedPreferences.getCurrency().toString()
                    )
                    == sharedPreferences.getExceedExpense().toString()
                ) {
                    _exceedsMessage.value = sharedPreferences.getExceedExpense()
                } else {
                    viewModelScope.launch {
                        val res = localRepository.insertExpense(
                            Expenses(
                                amount = amount.toBigDecimal(),
                                description = description,
                                expenseCategory = category,
                                currency = sharedPreferences.getCurrency(),
                                date = date
                            )
                        )
                        Log.d("res", res.toString())
                    }
                    _validationMsg.value = true
                }
            }
        }
    }

    fun addNewCategory(category: Categories) {
        viewModelScope.launch {
            localRepository.insertNewCategory(listOf(category))
        }
    }
}
