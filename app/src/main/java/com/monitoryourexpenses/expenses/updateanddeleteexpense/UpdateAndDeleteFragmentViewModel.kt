package com.monitoryourexpenses.expenses.updateanddeleteexpense

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.monitoryourexpenses.expenses.Event
import com.monitoryourexpenses.expenses.R
import com.monitoryourexpenses.expenses.database.Expenses
import com.monitoryourexpenses.expenses.database.LocalRepository
import com.monitoryourexpenses.expenses.prefs.ExpenseMonitorSharedPreferences
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

class UpdateAndDeleteFragmentViewModel @ViewModelInject constructor(
    private var localRepository: LocalRepository,
    var expenseMonitorSharedPreferences : ExpenseMonitorSharedPreferences) : ViewModel() {

    var currency     = MutableLiveData<String>()
    var category    = MutableLiveData<String>()


    init {
        currency.value    =  expenseMonitorSharedPreferences.getCurrency()
    }
    val categories = localRepository.getAllCategories()

    private val _snackBarText = MutableLiveData<Event<Int>>()
    val snackBarText: LiveData<Event<Int>>
        get() = _snackBarText

    private val _exceedsMessage = MutableLiveData<String>()
    val exceedsMessage: LiveData<String>
        get() = _exceedsMessage

    private val _updatedExpenseEvent = MutableLiveData<Event<Unit>>()
    val updatedExpenseEvent: LiveData<Event<Unit>> = _updatedExpenseEvent


    fun expense(id:String): LiveData<Expenses> = localRepository.getExpense(id)

    @ExperimentalCoroutinesApi
    fun deleteExpense(id:String) {
        viewModelScope.launch {
            localRepository.deleteExpenseUsingId(id)
            _snackBarText.value = Event(R.string.delete_expense)
            _updatedExpenseEvent.value = Event(Unit)
        }
    }

    fun updateExpense(expenseDescription:String,expenseAmount:String,expenseDate:String,id:String) {
        val expenseCategory     = category.value
        val expenseCurrency     = currency.value

        if (expenseDescription.isEmpty() || expenseAmount.isEmpty()) {
            _snackBarText.value = Event(R.string.fill_empty)
        } else {
            viewModelScope.launch {
                if (localRepository.totalOfCurrentExpenses()) {
                    _exceedsMessage.value = expenseMonitorSharedPreferences.getExceedExpense()
                } else {
                    viewModelScope.launch {
                        id.let {id->
                            expenseCurrency?.let { currency ->
                                expenseDate.let { date ->
                                    expenseCategory?.let { category ->
                                        expenseAmount.toBigDecimal().let { amount ->
                                            expenseDescription.let { description ->
                                                localRepository.updateExpenseUsingId(
                                                    id,
                                                    amount,
                                                    description,
                                                    category,
                                                    date,
                                                    currency
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    _snackBarText.value = Event(R.string.update_expense)
                    _updatedExpenseEvent.value = Event(Unit)
                }
            }
        }
    }

    fun saveExceedExpense(exceedExpense:String){
        if (exceedExpense.isEmpty()){
            _snackBarText.value = Event(R.string.enter_fixed_expense)
        }else{
            expenseMonitorSharedPreferences.saveExceededExpenseForSettings(exceedExpense)
        }
    }


    fun cancelExpense(){
        expenseMonitorSharedPreferences.clearExpense()
    }

}
