package com.expensemoitor.expensemonitor.utilites

import android.content.Context
import android.content.SharedPreferences

class PrefManager(context: Context)
{
    private var pref: SharedPreferences
    private var editor: SharedPreferences.Editor
    // shared pref mode
    private var PRIVATE_MODE = 0

    init {
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref.edit()
    }


    fun saveAccessTokenAndCurrentExpense(currentExpense : String,access_token: String) {
        editor.putString(ACCESS_TOKEN,access_token)
        editor.putString(CURRENT_EXPENSE,currentExpense)
        editor.commit()
    }


    companion object
    {
        // Shared preferences constants
        private const val PREF_NAME = "MyPreference"
        private val ACCESS_TOKEN = "access_token"
        private val CURRENT_EXPENSE = "current_expense"
    }


}