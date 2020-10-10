package com.monitoryourexpenses.expenses.createexpense

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.monitoryourexpenses.expenses.MainCoroutineRule
import com.monitoryourexpenses.expenses.database.LocalRepository
import com.monitoryourexpenses.expenses.database.local.ExpenseMonitorDao
import com.monitoryourexpenses.expenses.prefs.ExpenseMonitorSharedPreferences
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.robolectric.annotation.Config


/**
 * Unit tests for the implementation of [CreateNewExpenseFragmentViewMode]
 */
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@Config(sdk = [Config.OLDEST_SDK])
class CreateNewExpenseFragmentViewModelTest{

    private lateinit var createNewExpenseFragmentViewModel: CreateNewExpenseFragmentViewModel

    private var expenseMonitorDao : ExpenseMonitorDao = mock(ExpenseMonitorDao::class.java)

    private lateinit var localRepository: LocalRepository

    private lateinit var expenseMonitorSharedPreferences: ExpenseMonitorSharedPreferences

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setViewModel() {
        localRepository                   = LocalRepository(expenseMonitorDao)
        expenseMonitorSharedPreferences   = ExpenseMonitorSharedPreferences(ApplicationProvider.getApplicationContext())
        createNewExpenseFragmentViewModel = CreateNewExpenseFragmentViewModel(localRepository,expenseMonitorSharedPreferences)
    }

//    @Test
//    fun saveNewExpensesToRepository_showsSuccessMessageUi(){
//        val expenseCategory     = "food"
//        val expenseDescription  = "expense for food"
//        val expensesAmount      = "1000"
//        val expenseDate         = "2020-10-10"
//        val expenseCurrency     = "SDG"
//
//        (createNewExpenseFragmentViewModel).apply {
//           category.value    = expenseCategory
//           description.value = expenseDescription
//           amount.value      = expensesAmount
//           currentDate.value = expenseDate
//           currency.value    = expenseCurrency
//        }
//
//        createNewExpenseFragmentViewModel.createNewExpense()
//
//        val newExpense = localRepository.selectAllExpenses()
//
//        assertThat(newExpense.currency).isEqualTo(expenseCurrency)
//        assertThat(newExpense.expenseCategory).isEqualTo(expenseCategory)
//        assertThat(newExpense.description).isEqualTo(expenseDescription)
//        assertThat(newExpense.amount).isEqualTo(expensesAmount)
//        assertThat(newExpense.date).isEqualTo(expenseDate)
//    }



}