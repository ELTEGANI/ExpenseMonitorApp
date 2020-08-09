package com.monitoryourexpenses.expenses.api

import com.monitoryourexpenses.expenses.data.ExpenseService
import com.monitoryourexpenses.expenses.utilites.AppConstants

object ApiFactory {

    val expensesService: ExpenseService = RetrofitFactory.retrofit(AppConstants.BASEURL)
        .create(ExpenseService::class.java)

    val ALL_EXPENSES:AllExpensesService = RetrofitFactory.retrofit((AppConstants.BASEURL))
        .create(AllExpensesService::class.java)

    val DELETE_EXPENSE:DeleteExpenseService = RetrofitFactory.retrofit(AppConstants.BASEURL)
        .create(DeleteExpenseService::class.java)

    val UPDATE_EXPENSE:UpdateExpenseService = RetrofitFactory.retrofit(AppConstants.BASEURL)
        .create(UpdateExpenseService::class.java)

}