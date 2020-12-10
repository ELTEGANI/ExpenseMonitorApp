package com.monitoryourexpenses.expenses.myexpenses

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.monitoryourexpenses.expenses.MainCoroutineRule
import com.monitoryourexpenses.expenses.createexpense.CreateNewExpenseFragmentViewModel
import com.monitoryourexpenses.expenses.database.LocalRepository
import com.monitoryourexpenses.expenses.database.local.ExpenseMonitorDao
import com.monitoryourexpenses.expenses.prefs.ExpenseMonitorSharedPreferences
import com.monitoryourexpenses.expenses.utilites.UtilitesFunctions
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.robolectric.annotation.Config


/**
 * Unit tests for the implementation of [MyExpenseFragmentViewModel]
 */

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@Config(sdk = [Config.OLDEST_SDK])
class MyExpenseFragmentViewModelTest {
    private lateinit var myExpenseFragmentViewModelTest: MyExpenseFragmentViewModel

    private var expenseMonitorDao : ExpenseMonitorDao = Mockito.mock(ExpenseMonitorDao::class.java)

    private lateinit var localRepository: LocalRepository

    private lateinit var expenseMonitorSharedPreferences: ExpenseMonitorSharedPreferences

    private lateinit var utilitesFunctions: UtilitesFunctions

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setViewModel() {
        utilitesFunctions                 = UtilitesFunctions(ApplicationProvider.getApplicationContext())
        expenseMonitorSharedPreferences   = ExpenseMonitorSharedPreferences(ApplicationProvider.getApplicationContext())
        localRepository                   = LocalRepository(expenseMonitorDao,expenseMonitorSharedPreferences)
        myExpenseFragmentViewModelTest    = MyExpenseFragmentViewModel(expenseMonitorSharedPreferences,
        localRepository,utilitesFunctions)
    }


}