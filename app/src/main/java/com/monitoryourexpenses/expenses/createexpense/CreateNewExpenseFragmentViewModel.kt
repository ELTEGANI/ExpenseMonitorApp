package com.monitoryourexpenses.expenses.createexpense

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.monitoryourexpenses.expenses.Event
import com.monitoryourexpenses.expenses.R
import com.monitoryourexpenses.expenses.database.Categories
import com.monitoryourexpenses.expenses.database.Expenses
import com.monitoryourexpenses.expenses.database.LocalRepository
import com.monitoryourexpenses.expenses.prefs.ExpenseMonitorSharedPreferences
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate

class CreateNewExpenseFragmentViewModel @ViewModelInject constructor(val localRepository: LocalRepository,
        val expenseMonitorSharedPreferences: ExpenseMonitorSharedPreferences) : ViewModel() {

    val amount      = MutableLiveData<String>()
    val description = MutableLiveData<String>()
    val currentDate = MutableLiveData<String>()
    var category    = MutableLiveData<String>()
    var currency    = MutableLiveData<String>()

    val categories = localRepository.getAllCategories()

    init {
        currentDate.value = LocalDate.now().toString()
        currency.value    =  expenseMonitorSharedPreferences.getCurrency()
    }

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>>
        get() = _snackbarText

    private val _exceedsMessage = MutableLiveData<String>()
    val exceedsMessage: LiveData<String>
        get() = _exceedsMessage

    private val _createdExpenseEvent = MutableLiveData<Event<Unit>>()
    val createdExpenseEvent: LiveData<Event<Unit>> = _createdExpenseEvent



    fun createNewExpense() {
        val expenseCategory     = category.value
        val expenseDescription  = description.value
        val expenseAmount       = amount.value
        val expenseDate         = currentDate.value
        val expenseCurrency     = currency.value

        if (expenseAmount == null || expenseDescription == null) {
            _snackbarText.value = Event(R.string.fill_empty)
        } else if (expenseCategory == null) {
            _snackbarText.value = Event(R.string.select_category)
        } else {
            viewModelScope.launch {
                if (localRepository.checkIfFixedExpenseExceeded()) {
                    _exceedsMessage.value = expenseMonitorSharedPreferences.getExceedExpense()
                } else {
                    viewModelScope.launch {
                        localRepository.insertExpense(
                            Expenses(
                                amount = expenseAmount.toBigDecimal(),
                                description = expenseDescription,
                                expenseCategory = expenseCategory,
                                currency = expenseCurrency,
                                date = expenseDate
                            )
                        )
                    }
                    _snackbarText.value = Event(R.string.expense_created_successfuly)
                    _createdExpenseEvent.value = Event(Unit)
                }
            }
        }
    }

    fun addNewCategory(category: String) {
        if (category.isNotEmpty()){
            val newCategory = Categories(id = null,CategoryName = category)
            viewModelScope.launch {
                localRepository.insertNewCategory(listOf(newCategory))
            }
        }else{
            _snackbarText.value = Event(R.string.cant_be_empty)
        }
    }

    fun saveExceedExpense(exceedExpense:String){
        if (exceedExpense.isEmpty()){
            _snackbarText.value = Event(R.string.enter_fixed_expense)
        }else{
            expenseMonitorSharedPreferences.saveExceededExpenseForSettings(exceedExpense)
        }
    }

    fun cancelExpense(){
        expenseMonitorSharedPreferences.clearExpense()
    }


}
