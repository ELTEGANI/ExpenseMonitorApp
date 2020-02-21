package com.monitoryourexpenses.expenses.loginuser

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.monitoryourexpenses.expenses.database.ExpenseMonitorDao
import com.monitoryourexpenses.expenses.database.LocalRepository
import com.monitoryourexpenses.expenses.utilites.PrefManager
import kotlinx.coroutines.launch


class LoginUserViewModel (var application: Application,val database: ExpenseMonitorDao) : ViewModel(){

    private val localRepository = LocalRepository(database)


    init {
        viewModelScope.launch {
            if(localRepository.checkIfExpensesIsEmpty(
                    PrefManager.getStartOfTheMonth(application).toString(),
                    PrefManager.getEndOfTheMonth(application).toString()
                ).isEmpty()
            ){
                //caches is empty request the server
               localRepository.getAllExpensesFromServer(PrefManager.getStartOfTheMonth(application).toString(),PrefManager.getEndOfTheMonth(application).toString())
            }
        }
    }

}