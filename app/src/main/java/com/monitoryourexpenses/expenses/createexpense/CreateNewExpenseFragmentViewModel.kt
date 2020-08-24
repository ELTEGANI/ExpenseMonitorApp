package com.monitoryourexpenses.expenses.createexpense


import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.preference.PreferenceManager
import com.monitoryourexpenses.expenses.R
import com.monitoryourexpenses.expenses.database.Categories
import com.monitoryourexpenses.expenses.database.ExpenseMonitorDao
import com.monitoryourexpenses.expenses.database.Expenses
import com.monitoryourexpenses.expenses.database.LocalRepository
import com.monitoryourexpenses.expenses.utilites.MyApp.Companion.context
import com.monitoryourexpenses.expenses.utilites.PrefManager
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate


class CreateNewExpenseFragmentViewModel(val database: ExpenseMonitorDao,var application: Application) : ViewModel() {

    private val localRepository = LocalRepository(database)
    val amount = MutableLiveData<String>()
    val description = MutableLiveData<String>()
    val currentDate = MutableLiveData<String>()
    val categories = localRepository.getAllCategories()

    init {
        currentDate.value = LocalDate.now().toString()
    }


    private val _validationMsg = MutableLiveData<String>()
    val validationMsg: LiveData<String>
        get() = _validationMsg

    private val _exceedsMessage = MutableLiveData<String>()
    val exceedsMessage: LiveData<String>
        get() = _exceedsMessage


    fun createNewExpense(amount:String, description:String, date:String, category:String) {
        //TODO validation for empty categories
        //TODO prevent navigation from go back when empty
        if (amount.isEmpty() || description.isEmpty() || category.isEmpty()) {
            _validationMsg.value = context?.getString(R.string.fill_empty)
        } else {
            viewModelScope.launch {
                if (localRepository.sumationOfSpecifiedExpenses(
                        PrefManager.getCurrency(application).toString()
                    )
                    == PreferenceManager.getDefaultSharedPreferences(application)
                        .getString("exceed_expense", null).toString()
                ) {
                    _exceedsMessage.value =
                        PreferenceManager.getDefaultSharedPreferences(application)
                            .getString("exceed_expense", null)
                } else {
                    viewModelScope.launch {
                        val res = localRepository.insertExpense(
                            Expenses(
                                amount = amount.toBigDecimal(),
                                description = description,
                                expenseCategory = category,
                                currency = PrefManager.getCurrency(context),
                                date = date
                            )
                        )
                        Log.d("res",res.toString())
                    }
                    _validationMsg.value = context?.getString(R.string.expense_created_successfuly)
                }
            }
        }
    }

    fun addNewCategory(category:Categories){
        viewModelScope.launch {
            localRepository.insertNewCategory(listOf(category))
        }
    }
}