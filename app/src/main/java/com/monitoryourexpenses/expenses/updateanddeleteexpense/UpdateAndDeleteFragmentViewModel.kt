package com.monitoryourexpenses.expenses.updateanddeleteexpense

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.monitoryourexpenses.expenses.database.Expenses
import com.monitoryourexpenses.expenses.database.LocalRepository
import com.monitoryourexpenses.expenses.prefs.ExpenseMonitorSharedPreferences
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

class UpdateAndDeleteFragmentViewModel @ViewModelInject constructor(
    private var localRepository: LocalRepository,
    var expenseMonitorSharedPreferences : ExpenseMonitorSharedPreferences) : ViewModel() {

    val categories = localRepository.getAllCategories()

    private val _validationMsg = MutableLiveData<Boolean>()
    val validationMsg: LiveData<Boolean>
        get() = _validationMsg

    private val _isExpenseDeleted = MutableLiveData<Boolean>()
    val isExpenseDeleted: LiveData<Boolean>
        get() = _isExpenseDeleted

    private val _exceedsMessage = MutableLiveData<String>()
    val exceedsMessage: LiveData<String>
        get() = _exceedsMessage


    @ExperimentalCoroutinesApi
    fun deleteExpense(expenseId: String) {
        viewModelScope.launch {
            localRepository.deleteExpenseUsingId(expenseId)
            _isExpenseDeleted.value = true
        }
    }

    fun updateExpense(expenseId: String, amount: String, description: String, date: String, category: String) {
        if (description.isEmpty() || date.isEmpty() || amount.isEmpty()) {
            _validationMsg.value = false
        } else {
            viewModelScope.launch {
                if (localRepository.sumationOfSpecifiedExpenses(
                        expenseMonitorSharedPreferences.getCurrency().toString())
                    == expenseMonitorSharedPreferences.getExceedExpense().toString()) {
                    _exceedsMessage.value = expenseMonitorSharedPreferences.getExceedExpense()
                } else {
                    viewModelScope.launch {
                        localRepository.updateExpenseUsingId(
                            expenseId,
                            amount,
                            description,
                            category,
                            date
                        )
                        _validationMsg.value = true
                    }
                }
            }
        }
    }
}
