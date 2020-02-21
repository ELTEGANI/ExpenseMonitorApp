package com.monitoryourexpenses.expenses.network



data class UserData(
    val userName:String,
    val emailAddress:String,
    val gender:String,
    val currency:String
)


data class RegisterationResponse (
    val accessToken:String
)

