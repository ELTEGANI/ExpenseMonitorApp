package com.expensemoitor.expensemonitor.utilites

import android.content.Context
import android.content.SharedPreferences

class PrefManager
{

    companion object {
        private fun getSharedPreferences(context: Context): SharedPreferences {
            return context.getSharedPreferences("APP_PREF", Context.MODE_PRIVATE)
        }

        fun saveAccessTokenAndCurrentExpense(context: Context, accessToken: String,userExpenses:String) {
            val editor = getSharedPreferences(context).edit()
            editor.putString("ACCESS_TOKEN", accessToken)
            editor.putString("USER_EXPENSES", userExpenses)
            editor.apply()
        }

        fun getAccessToken(context: Context): String? {
            return getSharedPreferences(context).getString("ACCESS_TOKEN", null)
        }
    }
}

