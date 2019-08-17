package com.expensemoitor.expensemonitor.network

import com.expensemoitor.expensemonitor.utilites.AppConstants

object ApiFactory {

    val CREATE_EXPENSE_URLS:CreateExpenseUrls = RetrofitFactory.retrofit(AppConstants.BASEURL)
        .create(CreateExpenseUrls::class.java)


    val registerationUrls:RegisterationUrls = RetrofitFactory.retrofit((AppConstants.BASEURL))
        .create(RegisterationUrls::class.java)

}