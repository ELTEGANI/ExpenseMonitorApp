package com.monitoryourexpenses.expenses.settings

import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputType
import androidx.navigation.fragment.findNavController
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.monitoryourexpenses.expenses.BuildConfig
import com.monitoryourexpenses.expenses.R
import com.monitoryourexpenses.expenses.utilites.PrefManager

class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
      setPreferencesFromResource(R.xml.settings, rootKey)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferenceManager.sharedPreferences.registerOnSharedPreferenceChangeListener(this)

        val buildVersion = findPreference<Preference>("buildversion")
        buildVersion?.summary = BuildConfig.VERSION_NAME

        val preference: EditTextPreference? = findPreference("exceed_expense")
        preference?.setOnBindEditTextListener { editText ->
            editText.inputType = InputType.TYPE_CLASS_NUMBER // set only numbers allowed to input
        }
    }

    override fun onResume() {
        super.onResume()
        preferenceManager.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceManager.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
       if (key != null && key != "exceed_expense") {
           sharedPreferences?.getString(key, null)?.substring(range = 0..2)?.let {
               PrefManager.saveCurrency(context, it)
           }
           // navigate back to main_menu expenses
           val direction = SettingsFragmentDirections.actionSettingsFragmentToMyExpenseFragment()
           findNavController().navigate(direction)
       }
    }
}
