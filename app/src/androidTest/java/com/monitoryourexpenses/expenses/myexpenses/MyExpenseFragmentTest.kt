package com.monitoryourexpenses.expenses.myexpenses

import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import com.jakewharton.threetenabp.AndroidThreeTen
import com.monitoryourexpenses.expenses.R
import com.monitoryourexpenses.expenses.createexpense.CreateNewExpenseFragment
import com.monitoryourexpenses.expenses.database.LocalRepository
import com.monitoryourexpenses.expenses.di.LocalRepositoryModule
import com.monitoryourexpenses.expenses.launchFragmentInHiltContainer
import com.monitoryourexpenses.expenses.prefs.ExpenseMonitorSharedPreferences
import com.monitoryourexpenses.expenses.usercurrency.GetUserCurrencyFragment
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.verify
import javax.inject.Inject


/**
 * Integration test for the MyExpenseFragmentTest screen.
 */
@RunWith(AndroidJUnit4::class)
@MediumTest
@ExperimentalCoroutinesApi
@UninstallModules(LocalRepositoryModule::class)
@HiltAndroidTest
class MyExpenseFragmentTest{
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var localRepository: LocalRepository

    @Inject
    lateinit var expenseMonitorSharedPreferences: ExpenseMonitorSharedPreferences

    @Before
    fun init() {
        initAndroidThreeTen()
        // Populate @Inject fields in test class
        hiltRule.inject()
    }


    @Test
    fun userHasSetCurrency_displayMyExpenses(){
        //Given -On the "Add Expense" screen.
        val navController = Mockito.mock(NavController::class.java)
        launchFragment(navController)

        //WHEN -valid currency
        expenseMonitorSharedPreferences.setUserCurrency(true)

        //THEN - Verify that we navigated to userCurrency screen.
        verify(navController).navigate(R.id.action_myExpenseFragment_to_userCurrencyFragment)
    }

    private fun initAndroidThreeTen() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
        AndroidThreeTen.init(appContext)
    }

    private fun launchFragment(navController: NavController?) {
        launchFragmentInHiltContainer<MyExpenseFragment>(R.style.Theme_ExpenseMonitor_DayNight){
            Navigation.setViewNavController(view!!, navController)
        }
    }

}