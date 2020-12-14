package com.monitoryourexpenses.expenses.usercurrency

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import com.monitoryourexpenses.expenses.MainCoroutineRule
import com.monitoryourexpenses.expenses.R
import com.monitoryourexpenses.expenses.assertSnackbarMessage
import com.monitoryourexpenses.expenses.createexpense.CreateNewExpenseFragmentViewModel
import com.monitoryourexpenses.expenses.database.LocalRepository
import com.monitoryourexpenses.expenses.database.local.ExpenseMonitorDao
import com.monitoryourexpenses.expenses.prefs.ExpenseMonitorSharedPreferences
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.robolectric.annotation.Config
import org.threeten.bp.LocalDate

/**
 * Unit tests for the implementation of [GetUserCurrencyUserViewModelTest]
 */
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@Config(sdk = [Config.OLDEST_SDK])
class GetUserCurrencyUserViewModelTest{

    private lateinit var getUserCurrencyUserViewModel: GetUserCurrencyUserViewModel

    private var expenseMonitorDao : ExpenseMonitorDao = Mockito.mock(ExpenseMonitorDao::class.java)

    private lateinit var localRepository: LocalRepository

    private lateinit var expenseMonitorSharedPreferences: ExpenseMonitorSharedPreferences

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setViewModel() {
        expenseMonitorSharedPreferences   = ExpenseMonitorSharedPreferences(ApplicationProvider.getApplicationContext())
        localRepository                   = LocalRepository(expenseMonitorDao,expenseMonitorSharedPreferences)
        getUserCurrencyUserViewModel = GetUserCurrencyUserViewModel(localRepository,expenseMonitorSharedPreferences,ApplicationProvider.getApplicationContext())
    }


    @Test
    fun saveUserCurrencyToSharedPreferenceAndInsertCategories() = mainCoroutineRule.runBlockingTest{
        val userCurrency = "SDG"

        (getUserCurrencyUserViewModel).apply {
            currency = userCurrency
        }

        //When saving userCurrency
        getUserCurrencyUserViewModel.saveUserCurrency()

        //Then Currency should be saved to sharedPreference
        assertThat(expenseMonitorSharedPreferences.getCurrency()).isEqualTo(userCurrency)
        assertThat(expenseMonitorSharedPreferences.hasCurrency()).isTrue()
        assertThat(getUserCurrencyUserViewModel.insertAllCategories()).isNotNull()
    }

    @Test
    fun saveUserCurrencyToSharedPreferenceAndInsertCategories_WrongSelection_error() = mainCoroutineRule.runBlockingTest{
         val userCurrency = "Select Your Currency"

        (getUserCurrencyUserViewModel).apply {
            currency = userCurrency
        }

        //When saving userCurrency
        getUserCurrencyUserViewModel.saveUserCurrency()

        //Then message should be select currency
        assertSnackbarMessage(getUserCurrencyUserViewModel.snackbarText,R.string.select_currency)
    }

    @Test
    fun insertAllCategories() = mainCoroutineRule.runBlockingTest{
           getUserCurrencyUserViewModel.insertAllCategories()
        assertThat(getUserCurrencyUserViewModel.insertAllCategories()).isNotNull()
    }

    @Test
    fun saveAllDatesInSharedPreferences() = mainCoroutineRule.runBlockingTest{
        getUserCurrencyUserViewModel.saveAllDates()
        assertThat(expenseMonitorSharedPreferences.getCurrentDate()).isEqualTo(LocalDate.now().toString())
        assertThat(expenseMonitorSharedPreferences.getStartOfTheWeek()).isEqualTo(LocalDate.now().toString())
        assertThat(expenseMonitorSharedPreferences.getEndOfTheWeek()).isEqualTo(LocalDate.now().plusDays(7).toString())
        assertThat(expenseMonitorSharedPreferences.getStartOfTheMonth()).isEqualTo(LocalDate.now().toString())
        assertThat(expenseMonitorSharedPreferences.getEndOfTheMonth()).isEqualTo(LocalDate.now().plusMonths(10).toString())
    }
}