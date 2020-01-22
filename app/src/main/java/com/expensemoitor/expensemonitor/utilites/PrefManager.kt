package com.expensemoitor.expensemonitor.utilites

import android.content.Context
import android.content.SharedPreferences

class PrefManager
{

    companion object {
        private fun getSharedPreferences(context: Context?): SharedPreferences? {
            return context?.getSharedPreferences("APP_PREF", Context.MODE_PRIVATE)
        }

        fun saveAccessToken(context: Context?, accessToken: String) {
            val editor = getSharedPreferences(context)?.edit()
            editor?.putString("ACCESS_TOKEN", accessToken)
            editor?.apply()
        }


        fun getAccessToken(context: Context): String? {
            return getSharedPreferences(context)?.getString("ACCESS_TOKEN", null)
        }


        fun saveName(context: Context,username:String){
            val editor = getSharedPreferences(context)?.edit()
            editor?.putString("USER_NAME",username)
            editor?.apply()
        }

        fun saveEmail(context: Context,email:String){
            val editor = getSharedPreferences(context)?.edit()
            editor?.putString("USER_EMAIL",email)
            editor?.apply()
        }

        fun savePhoto(context: Context,photo:String){
            val editor = getSharedPreferences(context)?.edit()
            editor?.putString("USER_PHOTO",photo)
            editor?.apply()
        }


        fun getName(context: Context?): String? {
            return getSharedPreferences(context)?.getString("USER_NAME", null)
        }

        fun getEmail(context: Context?): String? {
            return getSharedPreferences(context)?.getString("USER_EMAIL", null)
        }

        fun saveCurrentDate(context: Context?, currentDate:String){
            val editor = getSharedPreferences(context)?.edit()
            editor?.putString("CURRENT_DATE",currentDate)
            editor?.apply()
        }

        fun saveEndOfTheWeek(context: Context?,endOfTheWeek:String){
            val editor = getSharedPreferences(context)?.edit()
            editor?.putString("END_OF_THE_WEEK",endOfTheWeek)
            editor?.apply()
        }

        fun saveStartOfTheWeek(context: Context?,startOfTheWeek:String){
            val editor = getSharedPreferences(context)?.edit()
            editor?.putString("START_OF_THE_WEEK",startOfTheWeek)
            editor?.apply()
        }

        fun saveEndOfTheMonth(context: Context?,endOfTheMonth:String){
            val editor = getSharedPreferences(context)?.edit()
            editor?.putString("END_OF_THE_MONTH",endOfTheMonth)
            editor?.apply()
        }


        fun saveStartOfTheMonth(context: Context?,startOfTheMonth:String){
            val editor = getSharedPreferences(context)?.edit()
            editor?.putString("START_OF_THE_MONTH",startOfTheMonth)
            editor?.apply()
        }


        fun getCurrentDate(context: Context?): String? {
            return getSharedPreferences(context)?.getString("CURRENT_DATE", null)
        }


        fun getEndOfTheWeek(context: Context?): String? {
            return getSharedPreferences(context)?.getString("END_OF_THE_WEEK", null)
        }

        fun getStartOfTheWeek(context: Context?): String? {
            return getSharedPreferences(context)?.getString("START_OF_THE_WEEK", null)
        }


        fun getEndOfTheMonth(context: Context?): String? {
            return getSharedPreferences(context)?.getString("END_OF_THE_MONTH", null)
        }

        fun getStartOfTheMonth(context: Context?): String? {
            return getSharedPreferences(context)?.getString("START_OF_THE_MONTH", null)
        }

        fun saveCurrency(context: Context?, selectedCurrency:String){
            val editor = getSharedPreferences(context)?.edit()
            editor?.putString("userCurrency",selectedCurrency)
            editor?.apply()
        }

        fun getCurrency(context: Context?): String? {
            return getSharedPreferences(context)?.getString("userCurrency", null)
        }


        fun setLoggedIn(context: Context, isAuthenticated:Boolean){
            val editor = getSharedPreferences(context)?.edit()
            editor?.putBoolean("IS_USER_AUTHENTICATED",isAuthenticated)
            editor?.apply()
        }

        fun isLoggedIn(context: Context): Boolean? {
            return getSharedPreferences(context)?.getBoolean("IS_USER_AUTHENTICATED",false)
        }


        fun setUserRegistered(context: Context?, isAuthenticated:Boolean){
            val editor = getSharedPreferences(context)?.edit()
                editor?.putBoolean("IS_USER_REGISTERED",isAuthenticated)
            editor?.apply()
        }

        fun isRegistered(context: Context): Boolean? {
            return getSharedPreferences(context)?.getBoolean("IS_USER_REGISTERED",false)
        }

        fun clear(context: Context?){
            val editor = getSharedPreferences(context)?.edit()
            editor?.remove("IS_USER_AUTHENTICATED")
            editor?.apply()
        }



    }
}

