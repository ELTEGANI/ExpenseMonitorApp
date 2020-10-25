package com.monitoryourexpenses.expenses.createexpense

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.monitoryourexpenses.expenses.MainCoroutineRule
import com.monitoryourexpenses.expenses.R
import com.monitoryourexpenses.expenses.assertSnackbarMessage
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
        expenseMonitorSharedPreferences   = ExpenseMonitorSharedPreferences(ApplicationProvider.getApplicationContext())
        localRepository                   = LocalRepository(expenseMonitorDao,expenseMonitorSharedPreferences)
        createNewExpenseFragmentViewModel = CreateNewExpenseFragmentViewModel(localRepository,expenseMonitorSharedPreferences)
    }

    @Test
    fun saveNewExpensesToRepository_showsSuccessMessageUi() {
        val expenseCategory     = "food"
        val expenseDescription  = "expense for food"
        val expensesAmount      = "1000"
        val expenseDate         = "2020-10-10"
        val expenseCurrency     = "SDG"

        (createNewExpenseFragmentViewModel).apply {
            category.value    = expenseCategory
            description.value = expenseDescription
            amount.value      = expensesAmount
            currentDate.value = expenseDate
            currency.value    = expenseCurrency
        }

        createNewExpenseFragmentViewModel.createNewExpense()
        assertSnackbarMessage(createNewExpenseFragmentViewModel.snackbarText, R.string.expense_created_successfuly)
    }

    @Test
    fun saveNewExpenseToRepository_nullExpensesAmount_error(){
        saveExpenseAndAssertSnackBarError(null,"sdf","food")
    }

    @Test
    fun saveNewExpenseToRepository_nullExpensesAmount_and_nullExpensesDescription_error(){
        saveExpenseAndAssertSnackBarError(null,null,"drink")
    }

    @Test
    fun saveNewExpenseToRepository_nullCategory_error(){
        saveExpenseAndAssertSnackBarErrorWhenCategoryIsNull("200","for my car",null)
    }

    private fun saveExpenseAndAssertSnackBarError(expensesAmount: String?, expenseDescription: String?,
                                                  expenseCategory: String?) {
        (createNewExpenseFragmentViewModel).apply {
           category.value    = expenseCategory
           description.value = expenseDescription
           amount.value      = expensesAmount
        }

        // When saving an incomplete task
        createNewExpenseFragmentViewModel.createNewExpense()

        // Then the snackBar shows an error
        assertSnackbarMessage(createNewExpenseFragmentViewModel.snackbarText,R.string.fill_empty)
    }

    @Test
    fun addNewCategory_emptyCategory(){
        val newCategory = ""
        createNewExpenseFragmentViewModel.addNewCategory(newCategory)
        assertSnackbarMessage(createNewExpenseFragmentViewModel.snackbarText,R.string.cant_be_empty)
    }

    @Test
    fun addNewCategory(){
        val newCategory = "Food"
        createNewExpenseFragmentViewModel.addNewCategory(newCategory)
    }

    @Test
    fun saveExceedExpense_emptyExceedExpense_error(){
        val fixedExpense = ""
        createNewExpenseFragmentViewModel.saveExceedExpense(fixedExpense)
        assertSnackbarMessage(createNewExpenseFragmentViewModel.snackbarText,R.string.enter_fixed_expense)
    }

    @Test
    fun saveExceedExpense(){
        val fixedExpense = "1000"
        createNewExpenseFragmentViewModel.saveExceedExpense(fixedExpense)
        assertThat(expenseMonitorSharedPreferences.getExceedExpense()).isEqualTo(fixedExpense)
    }

    private fun saveExpenseAndAssertSnackBarErrorWhenCategoryIsNull(expensesAmount: String?, expenseDescription: String?,
                                                  expenseCategory: String?) {
        (createNewExpenseFragmentViewModel).apply {
            category.value    = expenseCategory
            description.value = expenseDescription
            amount.value      = expensesAmount
        }

        // When saving an incomplete task
        createNewExpenseFragmentViewModel.createNewExpense()

        // Then the snackBar shows an error
        assertSnackbarMessage(createNewExpenseFragmentViewModel.snackbarText,R.string.select_category)
    }


}