package com.expensemoitor.expensemonitor.utilites

import android.content.Context
import android.content.SharedPreferences

class PrefManager
{

    companion object {
        private fun getSharedPreferences(context: Context): SharedPreferences {
            return context.getSharedPreferences("APP_PREF", Context.MODE_PRIVATE)
        }

        fun saveAccessTokenAndCurrentExpense(context: Context, accessToken: String,todayExpenses:Int,weekExpense:Int,monthExpense:Int) {
            val editor = getSharedPreferences(context).edit()
            editor.putString("ACCESS_TOKEN", accessToken)
            editor.putInt("TODAY_EXPENSES", todayExpenses)
            editor.putInt("WEEK_EXPENSES", weekExpense)
            editor.putInt("MONTH_EXPENSES", monthExpense)
            editor.apply()
        }

        fun getAccessToken(context: Context): String? {
            return getSharedPreferences(context).getString("ACCESS_TOKEN", null)
        }

        fun getUserExpenses(context: Context):Int?{
            return getSharedPreferences(context).getInt("TODAY_EXPENSES",0)
        }

        fun saveUpdatedTodayExpense(context: Context, updatedTodayExpenses:Int){
            val editor = getSharedPreferences(context).edit()
            editor.putInt("TODAY_EXPENSES",updatedTodayExpenses)
            editor.apply()
        }

        fun saveUpdatedWeekExpense(context: Context, updatedWeekExpenses:Int){
            val editor = getSharedPreferences(context).edit()
            editor.putInt("WEEK_EXPENSES",updatedWeekExpenses)
            editor.apply()
        }

        fun saveUpdatedMonthExpense(context: Context, updatedMonthExpenses:Int){
            val editor = getSharedPreferences(context).edit()
            editor.putInt("MONTH_EXPENSES",updatedMonthExpenses)
            editor.apply()
        }

        fun saveCurrency(context: Context,userCurrency:String){
            val editor = getSharedPreferences(context).edit()
            editor.putString("USER_CURRENCY",userCurrency)
            editor.apply()
        }

        fun saveStartOfWeek(context: Context,startWeek:String){
            val editor = getSharedPreferences(context).edit()
            editor.putString("START_OF_THE_WEEK",startWeek)
            editor.apply()
        }

        fun getStartOfWeek(context: Context): String? {
            return getSharedPreferences(context).getString("START_OF_THE_WEEK", null)
        }

        fun saveEndOfWeek(context: Context,endWeek:String){
            val editor = getSharedPreferences(context).edit()
            editor.putString("END_OF_THE_WEEK",endWeek)
            editor.apply()
        }

        fun getEndOfWeek(context: Context): String? {
            return getSharedPreferences(context).getString("END_OF_THE_WEEK", null)
        }

        fun getCurrency(context: Context): String? {
            return getSharedPreferences(context).getString("USER_CURRENCY", null)
        }


        fun setLoggedIn(context: Context, isAuthenticated:Boolean){
            val editor = getSharedPreferences(context).edit()
            editor.putBoolean("IS_USER_AUTHENTICATED",isAuthenticated)
            editor.apply()
        }

        fun isLoggedIn(context: Context): Boolean? {
            return getSharedPreferences(context).getBoolean("IS_USER_AUTHENTICATED",false)
        }


        fun setUserRegistered(context: Context, isAuthenticated:Boolean){
            val editor = getSharedPreferences(context).edit()
                editor.putBoolean("IS_USER_REGISTERED",isAuthenticated)
            editor.apply()
        }

        fun isRegistered(context: Context): Boolean? {
            return getSharedPreferences(context).getBoolean("IS_USER_REGISTERED",false)
        }



    }
}

