package com.expensemoitor.expensemonitor.settings

import android.content.SharedPreferences
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceFragmentCompat
import com.expensemoitor.expensemonitor.R
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


       if(!key.equals(getCurrencyFromSettings())){
           //navigate back to main expenses
           val direction = SettingsFragmentDirections.actionSettingsFragmentToMyExpenseFragment()
           findNavController().navigate(direction)
       }
    }


}
