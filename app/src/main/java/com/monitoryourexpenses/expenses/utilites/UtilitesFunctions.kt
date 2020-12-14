package com.monitoryourexpenses.expenses.utilites

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.graphics.Color
import android.net.ConnectivityManager
import android.view.Gravity
import android.widget.Toast
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.res.use
import androidx.preference.PreferenceManager
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UtilitesFunctions @Inject constructor(@ApplicationContext var context: Context){
    fun expenseAmountFormatter(amount: String?): String {
        var amountFormatted = ""
        if (amount != null) {
            try {
                val value = amount.replace(",", "")
                val reverseValue = StringBuilder(value).reverse()
                    .toString()
                val finalValue = StringBuilder()
                for (i in 1..reverseValue.length) {
                    val `val` = reverseValue[i - 1]
                    finalValue.append(`val`)
                    if (i % 3 == 0 && i != reverseValue.length && i > 0) {
                        finalValue.append(",")
                    }
                }
                amountFormatted = finalValue.reverse().toString()
            } catch (e: Exception) {
                // Do nothing since not a number
            }
            return amountFormatted
        } else {
            return "0"
        }
    }

    fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting
    }

    fun toast(message: CharSequence) {
        val toast = Toast.makeText(context, message, Toast.LENGTH_LONG)
        toast.setGravity(Gravity.BOTTOM, 0, 600)
        toast.show()
    }

    @ColorInt
    @SuppressLint("Recycle")
    fun themeColor(@AttrRes themeAttrId: Int): Int {
        return context.obtainStyledAttributes(intArrayOf(themeAttrId)).use {
            it.getColor(0, Color.WHITE)
        }
    }
}
