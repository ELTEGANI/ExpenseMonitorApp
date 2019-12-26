package com.expensemoitor.expensemonitor.myexpenses



import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
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
import com.google.android.material.tabs.TabLayoutMediator


class MyExpenseFragment : Fragment() {

    private lateinit var binding: MyExpenseFragmentBinding
    lateinit var viewModel: MyExpenseFragmentViewModel
    private var mGoogleSignInClient: GoogleSignInClient? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.my_expense_fragment,container,false)



        val application = requireNotNull(this.activity).application
        val dataBase = ExpenseMonitorDataBase.getInstance(application).expenseMonitorDao
        val viewModelFactory = MyExpenseFragmentViewModelFactory(dataBase,application)

        viewModel = ViewModelProviders.of(this,viewModelFactory)
            .get(MyExpenseFragmentViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this


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
                when(position.toString()){
                    "0"->{
//                       viewModel.expense.value = getCurrencyFromSettings()+" "+expenseFormat(PrefManager.getTodayExpenses(context)?.toString())
                       binding.dateTextView.text = getCurrentDate()
                    }
                    "1"->{
                        val weekDates = getStartAndEndOfTheWeek().split("*")
//                        viewModel.expense.value = getCurrencyFromSettings()+" "+expenseFormat(PrefManager.getWeeKExpenses(context)?.toString())
                        binding.dateTextView.text = weekDates[0]+" "+"/"+" "+weekDates[1]
                    }
                    "2"->{
                        val monthDates = getTheStartAndTheEndOfTheMonth().split("*")
//                        viewModel.expense.value = getCurrencyFromSettings()+" "+expenseFormat(PrefManager.getMonthExpenses(context)?.toString())
                        binding.dateTextView.text = monthDates[0]+" "+"/"+" "+monthDates[1]
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
