package com.monitoryourexpenses.expenses.createexpense


import android.content.Context
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import com.jakewharton.threetenabp.AndroidThreeTen
import com.monitoryourexpenses.expenses.R
import com.monitoryourexpenses.expenses.database.Categories
import com.monitoryourexpenses.expenses.database.LocalRepository
import com.monitoryourexpenses.expenses.di.LocalRepositoryModule
import com.monitoryourexpenses.expenses.launchFragmentInHiltContainer
import com.monitoryourexpenses.expenses.myexpenses.MyExpenseFragmentDirections
import com.monitoryourexpenses.expenses.prefs.ExpenseMonitorSharedPreferences
import com.monitoryourexpenses.expenses.prefs.ExpenseMonitorSharedPreferences_Factory
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.threeten.bp.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject



/**
 * Integration test for the CreateNewExpense screen.
 */
@RunWith(AndroidJUnit4::class)
@MediumTest
@ExperimentalCoroutinesApi
@UninstallModules(LocalRepositoryModule::class)
@HiltAndroidTest
class CreateNewExpenseFragmentTest{

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
        addCategories()
    }

    @Test
    fun emptyExpense_isNotSaved(){
        //Given -On the "Add Expense" screen.
        val navController = Mockito.mock(NavController::class.java)
        launchFragment(navController)

        //WHEN -inValid amount,date,category and description combination and click save
        onView(withId(R.id.expense_description_textView)).perform(clearText())
        onView(withId(R.id.expense_amount_textView)).perform(clearText())
        onView(withId(R.id.create_new_expense_button)).perform(click())

        // THEN - Entered Expense is still displayed
        onView(withId(R.id.expense_description_textView)).check(matches(isDisplayed()))
    }

    @Test
    fun validExpense_isSaved(){
        //Given -On the "Add Expense" screen.
        val navController = Mockito.mock(NavController::class.java)
        launchFragment(navController)

        //WHEN -Valid amount,date,category and description combination and click save
        onView(withId(R.id.categoryList)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(1,click()))
        onView(withId(R.id.expense_description_textView)).perform(replaceText("For Food"))
        onView(withId(R.id.expense_amount_textView)).perform(replaceText("1000"))
        onView(withId(R.id.expense_date_textView)).perform(replaceText(LocalDate.now().toString()))
        onView(withId(R.id.create_new_expense_button)).perform(click())

        //THEN - Verify that the repository saved the expense
        val expenses = localRepository.getTodayExpenses()?.value
        assertEquals(expenses?.size, 1)
        assertEquals(expenses?.get(0)?.amount, "1000")
        assertEquals(expenses?.get(0)?.description, "For Food")
        assertEquals(expenses?.get(0)?.date, LocalDate.now().toString())
    }


    @Test
    fun validExpense_navigatesBack(){
        //Given -On the "Add Expense" screen.
        val navController = Mockito.mock(NavController::class.java)
        launchFragment(navController)

        //WHEN -Valid amount,date,category and description combination and click save
        onView(withId(R.id.add_new_category)).perform(click())
        onView(withId(R.id.editTextTextNewCategory)).perform(replaceText("Food"))
        onView(withText(R.string.ok)).perform(click())
        onView(withId(R.id.categoryList)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(1,click()))
        onView(withId(R.id.expense_description_textView)).perform(replaceText("For Food"))
        onView(withId(R.id.expense_amount_textView)).perform(replaceText("1000"))
        onView(withId(R.id.expense_date_textView)).perform(replaceText(LocalDate.now().toString()))
        onView(withId(R.id.create_new_expense_button)).perform(click())

        //THEN - Verify that we navigated back to the MyExpenses screen.
        verify(navController).navigate(R.id.action_createNewExpenseFragment_to_myExpenseFragment)
    }

    private fun launchFragment(navController: NavController?) {
        launchFragmentInHiltContainer<CreateNewExpenseFragment>(R.style.Theme_ExpenseMonitor_DayNight) {
            Navigation.setViewNavController(view!!, navController)
        }
    }

    private fun initAndroidThreeTen() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
        AndroidThreeTen.init(appContext)
    }

    private fun addCategories(){
        runBlocking {
            localRepository.insertNewCategory(listOf(Categories(null,"Food"),Categories(null,"Home"),
            Categories(null,"Kids")))
        }
    }
}