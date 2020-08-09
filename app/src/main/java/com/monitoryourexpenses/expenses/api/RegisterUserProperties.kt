package com.monitoryourexpenses.expenses.api



data class UserData(
    val userName:String,
    val emailAddress:String,
    val gender:String,
    val currency:String
)


data class RegisterationResponse (
    val accessToken:String
)