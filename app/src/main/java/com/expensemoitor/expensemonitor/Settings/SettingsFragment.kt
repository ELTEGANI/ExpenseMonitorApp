package com.expensemoitor.expensemonitor.Settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.expensemoitor.expensemonitor.R


class SettingsFragment : PreferenceFragmentCompat() {


    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
      setPreferencesFromResource(R.xml.settings,rootKey)




    }
}
