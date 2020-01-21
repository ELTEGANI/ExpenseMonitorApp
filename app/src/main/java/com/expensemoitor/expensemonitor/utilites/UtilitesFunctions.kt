package com.expensemoitor.expensemonitor.utilites

import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import androidx.preference.PreferenceManager
import com.expensemoitor.expensemonitor.network.DurationExpenseResponse
import com.expensemoitor.expensemonitor.utilites.MyApp.Companion.context
import java.math.BigDecimal




fun expenseAmountFormater(amount: String?): String {
    var amountFormatted= ""
    if (amount != null){
        try {
            val value = amount?.replace(",", "")
            val reverseValue = StringBuilder(value.toString()).reverse()
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


   fun sumationOfAmount(expensesResponseList: List<DurationExpenseResponse>?): BigDecimal {
       val amountList = arrayListOf<BigDecimal>()
       return if (expensesResponseList != null) {
           expensesResponseList.forEach { amount ->
               amount.amount?.toBigDecimal()?.let { amountList.add(it) }
           }
           amountList.sumByBigDecimal()
       }else{
           BigDecimal.ZERO
       }
   }

fun saveCurrencyForSettings(selectedCurrency:String){
    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    val editor = sharedPreferences.edit()
    editor.putString("userCurrency",selectedCurrency)
    editor.apply()
}


fun Iterable<BigDecimal>.sumByBigDecimal(): BigDecimal {
    return this.fold(BigDecimal.ZERO) { acc, e -> acc + e }
}

fun hasNetworkConnection(): Boolean {
    val connectivityManager =
        context?.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetworkInfo = connectivityManager.activeNetworkInfo
    return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting
}
