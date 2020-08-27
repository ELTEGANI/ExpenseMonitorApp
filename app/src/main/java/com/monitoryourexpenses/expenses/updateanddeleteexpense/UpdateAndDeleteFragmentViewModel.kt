package com.monitoryourexpenses.expenses.updateanddeleteexpense

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.preference.PreferenceManager
import com.monitoryourexpenses.expenses.R
import com.monitoryourexpenses.expenses.database.ExpenseMonitorDao
import com.monitoryourexpenses.expenses.database.Expenses
import com.monitoryourexpenses.expenses.database.LocalRepository
import com.monitoryourexpenses.expenses.utilites.PrefManager
import kotlinx.coroutines.*

class UpdateAndDeleteFragmentViewModel(val expenses: Expenses, application: Application, dataBase: ExpenseMonitorDao) : AndroidViewModel(application) {

    private val application = getApplication<Application>().applicationContext
    private val localRepository = LocalRepository(dataBase)
    val categories = localRepository.getAllCategories()

    private val _selectedExpense = MutableLiveData<Expenses>()
    val selectedExpenseMsg :LiveData<Expenses>
        get() = _selectedExpense

    private val _validationMsg = MutableLiveData<Boolean>()
    val validationMsg: LiveData<Boolean>
        get() = _validationMsg

    private val _isExpenseDeleted = MutableLiveData<Boolean>()
    val isExpenseDeleted: LiveData<Boolean>
        get() = _isExpenseDeleted

    private val _exceedsMessage = MutableLiveData<String>()
    val exceedsMessage: LiveData<String>
        get() = _exceedsMessage

    val currentDate = MutableLiveData<String>()

    init {
        _selectedExpense.value = expenses
        currentDate.value = expenses.date
    }

    @ExperimentalCoroutinesApi
    fun deleteExpense(expenseId:String){
        viewModelScope.launch {
            localRepository.deleteExpneseUsingId(expenseId)
            _isExpenseDeleted.value = true
        }
    }



    fun updateExpense(expenseId:String,amount:String, description:String, date:String, category:String){
        if(description.isEmpty() || date.isEmpty() || amount.isEmpty()){
            _validationMsg.value =  false
        }else{
            viewModelScope.launch {
                if (localRepository.sumationOfSpecifiedExpenses(
                        PrefManager.getCurrency(application).toString())
                    == PreferenceManager.getDefaultSharedPreferences(application)
                        .getString("exceed_expense", null).toString()) {
                    _exceedsMessage.value = PreferenceManager.getDefaultSharedPreferences(application).getString("exceed_expense",null)
                } else {
                    viewModelScope.launch {
                        localRepository.updateExpenseUsingId(
                            expenseId,
                            amount,
                            description,
                            category,
                            date
                        )
                        _validationMsg.value =  true
                    }
                }
            }
        }
    }


}
