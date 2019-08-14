package com.expensemoitor.expensemonitor.network

import com.expensemoitor.expensemonitor.utilites.AppConstants

object ApiFactory {

    val expenseUrls:ExpenseUrls = RetrofitFactory.retrofit(AppConstants.BASEURL)
        .create(ExpenseUrls::class.java)

}