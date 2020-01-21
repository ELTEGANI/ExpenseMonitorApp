package com.expensemoitor.expensemonitor.settings

import android.content.SharedPreferences
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.expensemoitor.expensemonitor.BuildConfig
import com.expensemoitor.expensemonitor.R
import com.expensemoitor.expensemonitor.utilites.PrefManager


class SettingsFragment : PreferenceFragmentCompat() , SharedPreferences.OnSharedPreferenceChangeListener{


    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
      setPreferencesFromResource(R.xml.settings,rootKey)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferenceManager.sharedPreferences.registerOnSharedPreferenceChangeListener(this)


        val buildVersion = findPreference<Preference>("buildversion")
        buildVersion?.summary = BuildConfig.VERSION_NAME

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

       if(sharedPreferences?.getString(key,null) != null){
           sharedPreferences.getString(key, null)?.substring(range = 0..2)?.let {
               PrefManager.saveCurrency(context,
                   it
               )
           }
           //navigate back to main expenses
           val direction = SettingsFragmentDirections.actionSettingsFragmentToMyExpenseFragment()
           findNavController().navigate(direction)

       }
    }
}

