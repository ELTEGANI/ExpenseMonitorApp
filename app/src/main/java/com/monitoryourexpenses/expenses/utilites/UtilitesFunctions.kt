package com.monitoryourexpenses.expenses.utilites

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.graphics.Color
import android.net.ConnectivityManager
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.preference.PreferenceManager
import com.monitoryourexpenses.expenses.utilites.MyApp.Companion.context
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.math.BigDecimal




fun expenseAmountFormatter(amount: String?): String {
    var amountFormatted= ""
    if (amount != null){
        try {
            val value = amount.replace(",", "")
            val reverseValue = StringBuilder(value).reverse()
                .toString()
            val finalValue = StringBuilder()
            for (i in 1..reverseValue.length)
            {
                val `val` = reverseValue[i - 1]
                finalValue.append(`val`)
                if (i % 3 == 0 && i != reverseValue.length && i > 0)
                {
                    finalValue.append(",")
                }
            }
            amountFormatted = finalValue.reverse().toString()
        } catch (e: Exception) {
            // Do nothing since not a number
        }
        return amountFormatted
    }else{
        return "0"
    }
}

fun saveCurrencyForSettings(selectedCurrency:String){
    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    val editor = sharedPreferences.edit()
    editor.putString("userCurrency",selectedCurrency)
    editor.apply()
}

fun isConnected(): Boolean {
    val connectivityManager =
        context?.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetworkInfo = connectivityManager.activeNetworkInfo
    return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting
}

fun Context.toast(message: CharSequence) {
    val toast = Toast.makeText(this, message, Toast.LENGTH_LONG)
    toast.setGravity(Gravity.BOTTOM, 0, 600)
    toast.show()
}