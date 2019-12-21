package com.expensemoitor.expensemonitor.network


data class UserData(
    val userName:String,
    val emailAddress:String,
    val gender:String,
    val currency:String,
    val startWeek: String?,
    val endWeek: String?,
    val startMonth: String?,
    val endMonth: String?
)




data class RegisterationResponse (
    val accessToken:String,
    val userCurrentExpense:String,
    val weekExpense:String,
    val monthExpense:String

)