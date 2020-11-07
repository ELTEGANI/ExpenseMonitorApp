package com.monitoryourexpenses.expenses.usercurrency

import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import com.jakewharton.threetenabp.AndroidThreeTen
import com.monitoryourexpenses.expenses.R
import com.monitoryourexpenses.expenses.database.LocalRepository
import com.monitoryourexpenses.expenses.di.LocalRepositoryModule
import com.monitoryourexpenses.expenses.launchFragmentInHiltContainer
import com.monitoryourexpenses.expenses.prefs.ExpenseMonitorSharedPreferences
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.Matchers.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.verify
import javax.inject.Inject


/**
 * Integration test for the GetUserCurrencyFragmentTest screen.
 */
@RunWith(AndroidJUnit4::class)
@MediumTest
@ExperimentalCoroutinesApi
@UninstallModules(LocalRepositoryModule::class)
@HiltAndroidTest
class GetUserCurrencyFragmentTest{
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
    fun validCurrency_navigatesBack(){
        //Given -On the "GetUser Currency" screen.
        val navController = Mockito.mock(NavController::class.java)
        launchFragment(navController)

        //WHEN -Valid currency and click save
        onView(withId(R.id.spinner)).perform(click())
        onData(allOf(`is`(instanceOf(String::class.java)), `is`("SDG Sudanese pound"))).perform(click())
        onView(withId(R.id.spinner)).check(matches(withSpinnerText(containsString("SDG Sudanese pound"))))
        onView(withId(R.id.next_button)).perform(click())

        //THEN - Verify that we navigated back to the MyExpenses screen.
        verify(navController).navigate(R.id.action_registeration_to_myExpense)
    }

    @Test
    fun validCurrency_isSaved(){
        //Given -On the "Add Expense" screen.
        val navController = Mockito.mock(NavController::class.java)
        launchFragment(navController)

        //WHEN -Valid currency and click save
        onView(withId(R.id.spinner)).perform(click())
        onData(allOf(`is`(instanceOf(String::class.java)), `is`("SDG Sudanese pound"))).perform(click())
        onView(withId(R.id.spinner)).check(matches(withSpinnerText(containsString("SDG Sudanese pound"))))
        onView(withId(R.id.next_button)).perform(click())

        //THEN - Verify that the repository saved the currency
        assertEquals(expenseMonitorSharedPreferences.getCurrency(),"SDG")
    }

    @Test
    fun emptyCurrency_isNotSaved(){
        //Given -On the "Add Expense" screen.
        val navController = Mockito.mock(NavController::class.java)
        launchFragment(navController)

        //WHEN -inValid currency and click save
        onView(withId(R.id.spinner)).perform(click())
        onData(allOf(`is`(instanceOf(String::class.java)), `is`("Select Your Currency"))).perform(click())
        onView(withId(R.id.spinner)).check(matches(withSpinnerText(containsString("Select Your Currency"))))
        onView(withId(R.id.next_button)).perform(click())

        // THEN - select currency is still displayed
        onView(withId(R.id.spinner)).check(matches(isDisplayed()))
    }

    private fun launchFragment(navController: NavController?) {
        launchFragmentInHiltContainer<GetUserCurrencyFragment>(R.style.Theme_ExpenseMonitor_DayNight) {
            Navigation.setViewNavController(view!!, navController)
        }
    }
}
private fun initAndroidThreeTen() {
    val appContext = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
    AndroidThreeTen.init(appContext)
}