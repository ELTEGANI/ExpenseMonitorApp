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
import com.monitoryourexpenses.expenses.prefs.ExpenseMonitorSharedPreferences
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment: PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    @Inject
    lateinit var expenseMonitorSharedPreferences: ExpenseMonitorSharedPreferences
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
            editText.inputType = InputType.TYPE_CLASS_NUMBER
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
               expenseMonitorSharedPreferences.saveCurrency(it)
           }
       }
          if (findNavController().currentDestination?.id == R.id.settingsFragment) {
            findNavController().navigate(R.id.action_settingsFragment_to_myExpenseFragment)
          }
    }
}
