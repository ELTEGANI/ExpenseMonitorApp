package com.expensemoitor.expensemonitor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil.setContentView
import com.expensemoitor.expensemonitor.utilites.PrefManager
import com.expensemoitor.expensemonitor.utilites.getStartAndEndOfTheWeek

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val weekDates = getStartAndEndOfTheWeek().split("*")
        PrefManager.saveStartOfWeek(applicationContext,weekDates[0])
        PrefManager.saveEndOfWeek(applicationContext,weekDates[1])


    }
}
