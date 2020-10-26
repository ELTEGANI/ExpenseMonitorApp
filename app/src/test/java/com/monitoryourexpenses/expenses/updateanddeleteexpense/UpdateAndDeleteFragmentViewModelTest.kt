package com.monitoryourexpenses.expenses.updateanddeleteexpense

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.monitoryourexpenses.expenses.MainCoroutineRule
import com.monitoryourexpenses.expenses.R
import com.monitoryourexpenses.expenses.assertSnackbarMessage
import com.monitoryourexpenses.expenses.database.Expenses
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


/**
 * Unit tests for the implementation of [UpdateAndDeleteFragmentViewModelTest]
 */
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@Config(sdk = [Config.OLDEST_SDK])
class UpdateAndDeleteFragmentViewModelTest{

    private lateinit var updateAndDeleteFragmentViewModel: UpdateAndDeleteFragmentViewModel

    private var expenseMonitorDao : ExpenseMonitorDao = Mockito.mock(ExpenseMonitorDao::class.java)

    private lateinit var localRepository: LocalRepository

    private lateinit var expenseMonitorSharedPreferences: ExpenseMonitorSharedPreferences

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setViewModel(){
        expenseMonitorSharedPreferences   = ExpenseMonitorSharedPreferences(ApplicationProvider.getApplicationContext())
        localRepository                   = LocalRepository(expenseMonitorDao,expenseMonitorSharedPreferences)
        updateAndDeleteFragmentViewModel  = UpdateAndDeleteFragmentViewModel(localRepository,expenseMonitorSharedPreferences)
    }

    @Test
    fun updateExpense_nullExpenseDescription()=mainCoroutineRule.runBlockingTest{
        val expenseCategory     = "food"
        val expenseDescription  = null
        val expensesAmount      = "1000"
        val expenseDate         = "2020-10-10"
        val expenseCurrency     = "SDG"

        (updateAndDeleteFragmentViewModel).apply {
            category.value    = expenseCategory
            description.value = expenseDescription
            amount.value      = expensesAmount
            currentDate.value = expenseDate
            currency.value    = expenseCurrency
        }

        updateAndDeleteFragmentViewModel.updateExpense()
        assertSnackbarMessage(updateAndDeleteFragmentViewModel.snackBarText,R.string.fill_empty)
    }

    @Test
    fun updateExpense_nullExpenseAmount()=mainCoroutineRule.runBlockingTest{
        val expenseCategory     = "food"
        val expenseDescription  = "expense for food"
        val expensesAmount      = null
        val expenseDate         = "2020-10-10"
        val expenseCurrency     = "SDG"

        (updateAndDeleteFragmentViewModel).apply {
            category.value    = expenseCategory
            description.value = expenseDescription
            amount.value      = expensesAmount
            currentDate.value = expenseDate
            currency.value    = expenseCurrency
        }

        updateAndDeleteFragmentViewModel.updateExpense()
        assertSnackbarMessage(updateAndDeleteFragmentViewModel.snackBarText,R.string.fill_empty)
    }
    
}