package com.expensemoitor.expensemonitor.settings

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.expensemoitor.expensemonitor.R
import com.expensemoitor.expensemonitor.utilites.MyApp
import com.expensemoitor.expensemonitor.utilites.PrefManager
import com.expensemoitor.expensemonitor.utilites.getCurrencyFromSettings


class SettingsFragment : PreferenceFragmentCompat() , SharedPreferences.OnSharedPreferenceChangeListener{


    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
      setPreferencesFromResource(R.xml.settings,rootKey)

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferenceManager.sharedPreferences.registerOnSharedPreferenceChangeListener(this)

    }

    override fun onResume() {
        super.onResume()
        preferenceManager.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceManager.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?,key:String?) {
        Toast.makeText(context,"old"+sharedPreferences?.getString("userCurrency",null)
            +"new"+ key,Toast.LENGTH_LONG).show()
//       if(!key.equals(getCurrencyFromSettings())){
//           PrefManager.saveUpdatedTodayExpense(context,0)
//           PrefManager.saveUpdatedWeekExpense(context,0)
//           PrefManager.saveUpdatedMonthExpense(context,0)
//       }
    }


}
