package com.expensemoitor.expensemonitor.network

import com.expensemoitor.expensemonitor.utilites.AppConstants

object ApiFactory {

    val CREATE_EXPENSE_SERVICE:CreateExpenseService = RetrofitFactory.retrofit(AppConstants.BASEURL)
        .create(CreateExpenseService::class.java)


    val REGISTERATION_SERVICE:RegisterationService = RetrofitFactory.retrofit((AppConstants.BASEURL))
        .create(RegisterationService::class.java)


    val GET_EXPNSES_BASED_ON_DURATION_SERVICE:GetExpenseBasedOnDurationsService = RetrofitFactory.retrofit((AppConstants.BASEURL))
        .create(GetExpenseBasedOnDurationsService::class.java)

}