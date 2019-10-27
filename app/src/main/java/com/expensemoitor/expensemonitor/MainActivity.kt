package com.expensemoitor.expensemonitor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.expensemoitor.expensemonitor.utilites.PrefManager
import com.expensemoitor.expensemonitor.utilites.getStartAndEndOfTheWeek
import com.expensemoitor.expensemonitor.utilites.getTheStartAndTheEndOfTheMonth

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)




        val weekDates = getStartAndEndOfTheWeek().split("*")

        Log.d("startofweek","\n"+"satrtweek"+weekDates[0]+"\n"+"endweek"+weekDates[1])

        PrefManager.saveStartOfWeek(applicationContext,weekDates[0])
        PrefManager.saveEndOfWeek(applicationContext,weekDates[1])


        val monthDates = getTheStartAndTheEndOfTheMonth().split("*")
        PrefManager.saveStartOfTheMonth(applicationContext,monthDates[0])
        PrefManager.saveEndOfTheMonth(applicationContext,monthDates[1])


    }
}
