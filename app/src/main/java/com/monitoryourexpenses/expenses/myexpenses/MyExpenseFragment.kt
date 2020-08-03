package com.monitoryourexpenses.expenses.myexpenses

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.monitoryourexpenses.expenses.R
import com.monitoryourexpenses.expenses.adapters.MONTH_EXPENSE_INDEX
import com.monitoryourexpenses.expenses.adapters.PagerAdapter
import com.monitoryourexpenses.expenses.adapters.TODAY_EXPENSE_INDEX
import com.monitoryourexpenses.expenses.adapters.WEEK_EXPENSE_INDEX
import com.monitoryourexpenses.expenses.database.ExpenseMonitorDataBase
import com.monitoryourexpenses.expenses.databinding.MyExpenseFragmentBinding
import com.monitoryourexpenses.expenses.utilites.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import org.threeten.bp.LocalDate


class MyExpenseFragment : Fragment() {

    private lateinit var binding: MyExpenseFragmentBinding
    lateinit var viewModel: MyExpenseFragmentViewModel
    private var mGoogleSignInClient: GoogleSignInClient? = null

    private val appUpdateManager: AppUpdateManager by lazy { AppUpdateManagerFactory.create(context) }
    private val appUpdatedListener: InstallStateUpdatedListener by lazy {
        object : InstallStateUpdatedListener {
            override fun onStateUpdate(installState: InstallState) {
                when {
                    installState.installStatus() == InstallStatus.DOWNLOADED -> popupSnackbarForCompleteUpdate()
                    installState.installStatus() == InstallStatus.INSTALLED -> appUpdateManager.unregisterListener(this)
                    else -> Log.d("Install",
                        installState.installStatus().toString()
                    )
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.my_expense_fragment,container,false)

        val application = requireNotNull(this.activity).application
        val dataBase = ExpenseMonitorDataBase.getInstance(application).expenseMonitorDao
        val viewModelFactory = MyExpenseFragmentViewModelFactory(dataBase,application)

        viewModel = ViewModelProvider(this,viewModelFactory)
            .get(MyExpenseFragmentViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        (activity as AppCompatActivity).setSupportActionBar(binding.bottomAppBar)
        setHasOptionsMenu(true)

        if(isConnected()){checkForAppUpdate()}

        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        mGoogleSignInClient = context?.let { GoogleSignIn.getClient(it, gso) }

        val tabLayout = binding.tabs
        val viewPager = binding.viewPager

        viewPager.adapter = PagerAdapter(this)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = getTabTitle(position)
        }.attach()


        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            @SuppressLint("SetTextI18n")
            override fun onPageSelected(position: Int){
                super.onPageSelected(position)
                when(position){
                    0->{
                        viewModel.todayExpense.observe(viewLifecycleOwner, Observer {
                        binding.expenseTextView.text = PrefManager.getCurrency(context)+" "+ it
                        })
                        binding.dateTextView.text = LocalDate.now().toString()
                    }
                    1->{
                        viewModel.weekExpense.observe(viewLifecycleOwner, Observer {
                        binding.expenseTextView.text = PrefManager.getCurrency(context)+" "+ it
                        })
                        binding.dateTextView.text = PrefManager.getStartOfTheWeek(context)+" "+"/"+" "+PrefManager.getEndOfTheWeek(context)
                    }
                    2->{
                        viewModel.monthExpense.observe(viewLifecycleOwner, Observer {
                        binding.expenseTextView.text = PrefManager.getCurrency(context)+" "+ it
                        })
                        binding.dateTextView.text = PrefManager.getStartOfTheMonth(context)+" "+"/"+" "+ PrefManager.getEndOfTheMonth(context)
                    }
                }
            }
        })


        viewModel.navigateToMyExpense.observe(viewLifecycleOwner, Observer {
            shouldNavigate->if (shouldNavigate){
            val navController = binding.root.findNavController()
            navController.navigate(R.id.action_myExpenseFragment_to_createNewExpenseFragment)
            viewModel.onNavigatedToMyExpense()
        }
        })

        return binding.root

    }

     private fun checkForAppUpdate() {
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo
         appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                // Request the update.
                try {
                    val installType = when {
                        appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE) -> AppUpdateType.IMMEDIATE
                        else -> null
                    }
                    if (installType == AppUpdateType.IMMEDIATE) appUpdateManager.registerListener(appUpdatedListener)

                    installType?.let {
                        appUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo,
                            it,
                            context as Activity?,
                            APP_UPDATE_REQUEST_CODE)
                    }
                } catch (e: IntentSender.SendIntentException) {
                    e.printStackTrace()
                }
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == APP_UPDATE_REQUEST_CODE) {
            if (resultCode != Activity.RESULT_OK) {
                Toast.makeText(context,
                    getString(R.string.app_failed_to_update),
                    Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun popupSnackbarForCompleteUpdate() {
        val snackbar = Snackbar.make(binding.coordinatorlayout,
            getString(R.string.update_download),
            Snackbar.LENGTH_INDEFINITE)
        snackbar.setAction(getString(R.string.restart)) { appUpdateManager.completeUpdate() }
        context?.let { ContextCompat.getColor(it, R.color.color_on_surface_emphasis_high) }?.let {
            snackbar.setActionTextColor(
                it
            )
        }
        snackbar.show()
    }


    override fun onResume() {
        super.onResume()
        appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
                if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                    popupSnackbarForCompleteUpdate()
                }
                try {
                    if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                        appUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo,
                            AppUpdateType.IMMEDIATE,
                            context as Activity?,
                            APP_UPDATE_REQUEST_CODE)
                    }
                } catch (e: IntentSender.SendIntentException) {
                    e.printStackTrace()
                }
            }
    }

    companion object {
        private const val APP_UPDATE_REQUEST_CODE = 1991
    }


    private fun getTabTitle(position: Int): String? {
        return when (position) {
            TODAY_EXPENSE_INDEX -> getString(R.string.today)
            WEEK_EXPENSE_INDEX -> getString(R.string.week)
            MONTH_EXPENSE_INDEX -> getString(R.string.month)
            else -> null
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.menu_dark_mode->{
                val mode = if ((resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) ==
                    Configuration.UI_MODE_NIGHT_NO) {
                    AppCompatDelegate.MODE_NIGHT_YES
                } else {
                    AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
                }
                AppCompatDelegate.setDefaultNightMode(mode)
                true
            }
            R.id.share_application->{
                shareApp()
                true
            }
            R.id.action_setting->{
                val action = MyExpenseFragmentDirections.actionMyExpenseFragmentToSettingsFragment()
                findNavController().navigate(action)
                true
            }
            R.id.action_sign_out -> {
                viewModel.clearPrefs()
                signOut()
                val navController = binding.root.findNavController()
                navController.navigate(R.id.action_myExpenseFragment_to_loginUserFragment)
                true
            }

            else -> false
        }

    private fun shareApp() {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                "https://play.google.com/store/apps/details?id=com.monitoryourexpenses.expenses"
            )
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }


    private fun signOut() {
        mGoogleSignInClient?.signOut()?.addOnCompleteListener{
        }
    }

}
