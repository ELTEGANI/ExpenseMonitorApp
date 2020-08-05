package com.monitoryourexpenses.expenses.network

import com.monitoryourexpenses.expenses.utilites.AppConstants
import retrofit2.http.Body
import retrofit2.http.POST

object ApiFactory {

    val expensesService:ExpenseService = RetrofitFactory.retrofit(AppConstants.BASEURL)
        .create(ExpenseService::class.java)

    val REGISTERATION_SERVICE:RegisterationService = RetrofitFactory.retrofit((AppConstants.BASEURL))
        .create(RegisterationService::class.java)

    val ALL_EXPENSES:AllExpensesService = RetrofitFactory.retrofit((AppConstants.BASEURL))
        .create(AllExpensesService::class.java)

    val DELETE_EXPENSE:DeleteExpenseService = RetrofitFactory.retrofit(AppConstants.BASEURL)
        .create(DeleteExpenseService::class.java)

    val UPDATE_EXPENSE:UpdateExpenseService = RetrofitFactory.retrofit(AppConstants.BASEURL)
        .create(UpdateExpenseService::class.java)

}