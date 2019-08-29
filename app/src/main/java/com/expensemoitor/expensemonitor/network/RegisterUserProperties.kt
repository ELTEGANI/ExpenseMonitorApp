package com.expensemoitor.expensemonitor.network

data class UserData(
    val username:String,
    val emailaddress:String,
    val gender:String
)




data class RegisterationResponse (
    val accesstoken:String,
    val userCurrentExpense:String
)