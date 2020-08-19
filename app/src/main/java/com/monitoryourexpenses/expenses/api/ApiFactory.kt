package com.monitoryourexpenses.expenses.api

import com.monitoryourexpenses.expenses.data.ExpenseService
import com.monitoryourexpenses.expenses.utilites.AppConstants

object ApiFactory {

    val expensesService: ExpenseService = RetrofitFactory.retrofit(AppConstants.BASEURL)
        .create(ExpenseService::class.java)

}