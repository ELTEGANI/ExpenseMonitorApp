package com.monitoryourexpenses.expenses.splashscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.monitoryourexpenses.expenses.MainActivity
import com.monitoryourexpenses.expenses.R
import com.monitoryourexpenses.expenses.databinding.ActivityLauncherBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*


@AndroidEntryPoint
class LauncherActivity : AppCompatActivity() {

    private lateinit var activityLauncherBinding : ActivityLauncherBinding
    private val activityScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityLauncherBinding = DataBindingUtil.setContentView(this, R.layout.activity_launcher)


            Glide.with(this)
                .load(R.mipmap.ic_launcher_round)
                .transform(CircleCrop())
                .into(activityLauncherBinding.imageView)

        activityScope.launch {
            delay(1000)
            startActivity(Intent(this@LauncherActivity,MainActivity::class.java))
            finish()
        }
    }
    override fun onPause() {
        activityScope.cancel()
        super.onPause()
    }
}
