package com.monitoryourexpenses.expenses.updateanddeleteexpense

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.preference.PreferenceManager
import com.monitoryourexpenses.expenses.R
import com.monitoryourexpenses.expenses.database.ExpenseMonitorDao
import com.monitoryourexpenses.expenses.database.Expenses
import com.monitoryourexpenses.expenses.database.LocalRepository
import com.monitoryourexpenses.expenses.api.ApiFactory
import com.monitoryourexpenses.expenses.api.ExpenseData
import com.monitoryourexpenses.expenses.data.ExpensesRepository
import com.monitoryourexpenses.expenses.utilites.MyApp.Companion.context
import com.monitoryourexpenses.expenses.utilites.PrefManager
import com.monitoryourexpenses.expenses.utilites.ProgressStatus
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import okio.IOException
import retrofit2.HttpException

class UpdateAndDeleteFragmentViewModel(val expenses: Expenses,val expensesRepository: ExpensesRepository,application: Application,dataBase: ExpenseMonitorDao) : AndroidViewModel(application) {

    private val application = getApplication<Application>().applicationContext
    private val localRepository = LocalRepository(dataBase)

    private val _selectedExpense = MutableLiveData<Expenses>()
    val selectedExpenseMsg :LiveData<Expenses>
        get() = _selectedExpense


    private val _status = MutableLiveData<ProgressStatus>()
    val status: LiveData<ProgressStatus>
        get() = _status


    private val _msgError = MutableLiveData<String>()
    val msgError:LiveData<String>
        get() = _msgError


    private val _validationMsg = MutableLiveData<String>()
    val validationMsg: LiveData<String>
        get() = _validationMsg

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
            expensesRepository.deleteExpense(expenseId)
                .onStart {_status.value = ProgressStatus.LOADING}
                .onCompletion { _status.value = ProgressStatus.DONE}
                .catch {
                        if (it is IOException){
                            _validationMsg.value =context?.getString(R.string.weak_internet_connection)
                        }else if (it is HttpException){
                            when(it.code()){
                                500->{
                            _validationMsg.value = context?.getString(R.string.try_later)
                                }
                            }
                        }
                }
                .collect{
                    localRepository.deleteExpneseUsingId(expenseId)
                    _msgError.value = context?.getString(R.string.expense_deleted_successfuly)
                }
        }
    }



    @ExperimentalCoroutinesApi
    fun updateExpense(expenseId:String, newAmount:String, description:String, date:String, category:String){
        if(description.isEmpty() || date.isEmpty() || category.isEmpty()){
            _msgError.value =  context?.getString(R.string.fill_empty)
        }else if(category == application.getString(R.string.SelectCategory)){
            _validationMsg.value = context?.getString(R.string.select_category)
        }else{
            viewModelScope.launch {
                if (localRepository.sumationOfSpecifiedExpenses(
                        PrefManager.getCurrency(application).toString())
                    == PreferenceManager.getDefaultSharedPreferences(application)
                        .getString("exceed_expense", null).toString()) {
                    _exceedsMessage.value = PreferenceManager.getDefaultSharedPreferences(application).getString("exceed_expense",null)
                } else {
                    viewModelScope.launch {
                    val expenseData = PrefManager.getCurrency(application)?.let {
                    ExpenseData(newAmount, description,date,it,category)
                    }
                    expenseData?.let {expensesRepository.updateExpense(expenseId,it)
                        .onStart { _status.value = ProgressStatus.LOADING}
                        .onCompletion {  _status.value = ProgressStatus.DONE }
                        .catch {
                            if (it is IOException){
                            _validationMsg.value =context?.getString(R.string.weak_internet_connection)
                        }else if (it is HttpException){
                            when(it.code()){
                                500->{
                                    _validationMsg.value = context?.getString(R.string.try_later)
                                }
                            }
                          }
                        }
                        .collect{ response->
                            if (response.message.isNotEmpty()) {
                              _msgError.value = context?.getString(R.string.expense_update_successfuly)
                                  localRepository.updateExpenseUsingId(
                                            expenseId,
                                            newAmount,
                                            description,
                                            category,
                                            date
                                  )
                               }
                           }
                        }
                    }
                }
            }
        }
    }

    fun onMsgErrorDisplayed(){
       _msgError.value = null
    }

    fun onValidationErrorDisplayed(){
        _validationMsg.value = null
    }

}
