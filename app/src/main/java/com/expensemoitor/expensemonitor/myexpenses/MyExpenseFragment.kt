package com.expensemoitor.expensemonitor.myexpenses



import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceManager
import androidx.viewpager2.widget.ViewPager2
import com.expensemoitor.expensemonitor.R
import com.expensemoitor.expensemonitor.database.ExpenseMonitorDataBase
import com.expensemoitor.expensemonitor.databinding.MyExpenseFragmentBinding
import com.expensemoitor.expensemonitor.utilites.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
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

        viewModel = ViewModelProviders.of(this,viewModelFactory)
            .get(MyExpenseFragmentViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this


        checkForAppUpdate()

        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        mGoogleSignInClient = context?.let { GoogleSignIn.getClient(it, gso) }



        val tabLayout = binding.tabs
        val viewPager = binding.viewPager


        viewPager.adapter = PagerAdapter(this)


        // Set the text for each tab
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = getTabTitle(position)
        }.attach()




        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int){
                super.onPageSelected(position)
                when(position){
                    0->{
                        viewModel.todayExpense.observe(this@MyExpenseFragment, Observer {
                        binding.expenseTextView.text = getCurrencyFromSettings()+" "+ expenseFormat(it)
                        })
                        binding.dateTextView.text = LocalDate.now().toString()
                    }
                    1->{
                        viewModel.weekExpense.observe(this@MyExpenseFragment, Observer {
                        binding.expenseTextView.text = getCurrencyFromSettings()+" "+expenseFormat(it)
                        })
                        binding.dateTextView.text = PrefManager.getStartOfTheWeek(context)+" "+"/"+" "+PrefManager.getEndOfTheWeek(context)
                    }
                    2->{
                        viewModel.monthExpense.observe(this@MyExpenseFragment, Observer {
                        binding.expenseTextView.text = getCurrencyFromSettings()+" "+expenseFormat(it)
                        })
                        binding.dateTextView.text = PrefManager.getStartOfTheMonth(context)+" "+"/"+" "+ PrefManager.getEndOfTheMonth(context)
                    }
                }
            }
        })



        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        setHasOptionsMenu(true)



        viewModel.navigateToMyExpense.observe(this, Observer {
            shouldNavigate->if (shouldNavigate){
            val navController = binding.root.findNavController()
            navController.navigate(R.id.action_myExpenseFragment_to_createNewExpenseFragment)
            viewModel.onNavigatedToMyExpense()
        }
        })

        setHasOptionsMenu(true)
        return binding.root

    }

     private fun checkForAppUpdate() {
        // Returns an intent object that you use to check for an update.
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        // Checks that the platform will allow the specified type of update.
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
                    "App Update failed, please try again on the next app launch.",
                    Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun popupSnackbarForCompleteUpdate() {
        val snackbar = Snackbar.make(binding.coordinatorlayout,
            "An update has just been downloaded.",
            Snackbar.LENGTH_INDEFINITE)
        snackbar.setAction("RESTART") { appUpdateManager.completeUpdate() }
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
                // If the update is downloaded but not installed,
                // notify the user to complete the update.
                if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                    popupSnackbarForCompleteUpdate()
                }
                //Check if Immediate update is required
                try {
                    if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                        // If an in-app update is already running, resume the update.
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
        inflater.inflate(R.menu.main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_setting->{
                val action = MyExpenseFragmentDirections.actionMyExpenseFragmentToSettingsFragment()
                findNavController().navigate(action)
            }
            R.id.action_sign_out ->{
                viewModel.clearPrefs()
                signOut()
                val navController = binding.root.findNavController()
                navController.navigate(R.id.action_myExpenseFragment_to_loginUserFragment)
                return true
            }
        }
        return false
    }


    private fun signOut() {
        mGoogleSignInClient?.signOut()?.addOnCompleteListener{
        }
    }



}
