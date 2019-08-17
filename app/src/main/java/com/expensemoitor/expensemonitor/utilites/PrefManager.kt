package com.expensemoitor.expensemonitor.utilites

import android.content.Context
import android.content.SharedPreferences

class PrefManager
{

    companion object {
        private fun getSharedPreferences(context: Context): SharedPreferences {
            return context.getSharedPreferences("APP_PREF", Context.MODE_PRIVATE)
        }

        fun saveAccessTokenAndCurrentExpense(context: Context, accessToken: String,userExpenses:Int) {
            val editor = getSharedPreferences(context).edit()
            editor.putString("ACCESS_TOKEN", accessToken)
            editor.putInt("USER_EXPENSES", userExpenses)
            editor.apply()
        }

        fun getAccessToken(context: Context): String? {
            return getSharedPreferences(context).getString("ACCESS_TOKEN", null)
        }

        fun getUserExpenses(context: Context):Int?{
            return getSharedPreferences(context).getInt("USER_EXPENSES",0)
        }

        fun saveUpdatedExpense(context: Context,updatedExpenses:Int){
            val editor = getSharedPreferences(context).edit()
            editor.putInt("USER_EXPENSES",updatedExpenses)
            editor.apply()
        }

        fun saveCurrency(context: Context,userCurrency:String){
            val editor = getSharedPreferences(context).edit()
            editor.putString("USER_CURRENCY",userCurrency)
            editor.apply()
        }

        fun getCurrency(context: Context): String? {
            return getSharedPreferences(context).getString("USER_CURRENCY", null)
        }

    }
}

