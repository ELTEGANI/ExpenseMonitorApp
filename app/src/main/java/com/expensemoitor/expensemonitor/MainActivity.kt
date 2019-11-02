package com.expensemoitor.expensemonitor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.expensemoitor.expensemonitor.utilites.PrefManager
import com.expensemoitor.expensemonitor.utilites.getCurrentDate

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        PrefManager.saveCurrentDate(application,getCurrentDate())


    }
}
