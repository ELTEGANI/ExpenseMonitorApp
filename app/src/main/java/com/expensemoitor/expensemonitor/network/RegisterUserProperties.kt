package com.expensemoitor.expensemonitor.network

data class UserData(
    val username:String,
    val emailaddress:String,
    val gender:String,
    val startWeek: String?,
    val endWeek: String?,
    val startMonth: String?,
    val endMonth: String?
)




data class RegisterationResponse (
    val accesstoken:String,
    val userCurrentExpense:String,
    val weekExpense:String,
    val monthExpense:String

)