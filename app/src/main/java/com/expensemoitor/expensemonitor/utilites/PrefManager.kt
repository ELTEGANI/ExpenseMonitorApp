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


        fun getCurrentDate(context: Context?): String? {
            return getSharedPreferences(context)?.getString("CURRENT_DATE", null)
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

