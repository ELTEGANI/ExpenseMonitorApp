package com.monitoryourexpenses.expenses.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.monitoryourexpenses.expenses.database.ExpenseMonitorDataBase
import com.monitoryourexpenses.expenses.database.Expenses
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.hamcrest.collection.IsCollectionWithSize.hasSize
import org.hamcrest.core.IsNull.notNullValue
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class ExpenseMonitorDaoTest {

    //Excute each task syn using arch component
    @get:Rule
    var instantTaskExecutorRule =InstantTaskExecutorRule()


    private lateinit var expenseMonitorDataBase:ExpenseMonitorDataBase

    @Before
    fun initDb(){
        expenseMonitorDataBase = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),
            ExpenseMonitorDataBase::class.java).build()
    }

    @After
    fun closeDataBase() = expenseMonitorDataBase.close()

    @Test
    fun insertExpense() = runBlocking {
        //GIVEN-insert Expense
        val expnense = Expenses(1,"1",2000.toBigDecimal(),"Wedding","Wedding","SDG","2020-02-28")
        expenseMonitorDataBase.expenseMonitorDao.insertExpenses(expnense)

        //WHEN-get the expense by id from database
        val loaded = expenseMonitorDataBase.expenseMonitorDao.selectAllExpenses()

        //THEN-the loaded data contains the expected data
        assertThat<List<Expenses>>(loaded, hasSize(1))
    }


    @Test
    fun insertExpensesForCachingData() = runBlocking {
        //GIVEN-insert Expense
        val expnense = listOf<Expenses>(
            Expenses(1,"1",2000.toBigDecimal(),"Wedding","Wedding","SDG","2020-02-28"),
            Expenses(2,"1",2000.toBigDecimal(),"Wedding","Wedding","SDG","2020-02-28"),
            Expenses(3,"1",2000.toBigDecimal(),"Wedding","Wedding","SDG","2020-02-28"),
            Expenses(4,"1",2000.toBigDecimal(),"Wedding","Wedding","SDG","2020-02-28")
        )
        expenseMonitorDataBase.expenseMonitorDao.insertExpensesForCachingData(*expnense.toTypedArray())

        //WHEN-get all expenses from database
        val loaded = expenseMonitorDataBase.expenseMonitorDao.selectAllExpenses()

        //THEN-the loaded data contains the expected data
        assertThat(loaded, hasSize(4))
    }

    @Test
     fun clearAllMonthExpenses() = runBlocking {
           //GIVEN-insert Expense
        val expnense = listOf<Expenses>(
            Expenses(1,"1",2000.toBigDecimal(),"Wedding","Wedding","SDG","2020-02-01"),
            Expenses(2,"1",2000.toBigDecimal(),"Wedding","Wedding","SDG","2020-02-02"),
            Expenses(3,"1",2000.toBigDecimal(),"Wedding","Wedding","SDG","2020-02-03")
        )
        expenseMonitorDataBase.expenseMonitorDao.insertExpensesForCachingData(*expnense.toTypedArray())


        //WHEN-get clear expenses from database
        val loaded = expenseMonitorDataBase.expenseMonitorDao.clearAllMonthExpenses("2020-02-01","2020-02-29")


        //THEN- clear all data in the database
        assertThat(loaded, `is`(3))
    }

    @Test
    fun updateExpense() = runBlocking {
        //GIVEN-insert Expense
        val expnense = listOf<Expenses>(
            Expenses(1,"1",2000.toBigDecimal(),"Wedding","Wedding","SDG","2020-02-01")
        )
        expenseMonitorDataBase.expenseMonitorDao.insertExpensesForCachingData(*expnense.toTypedArray())

        //WHEN-update expense in database by id
        val loaded = expenseMonitorDataBase.expenseMonitorDao.updateExpense("1","400","Death","Occastions","2020-02-01")

        //THEN- update expense in the database
        assertThat(loaded, `is`(1))
    }

    @Test
    fun deleteExpenses() = runBlocking {
        //GIVEN-insert Expense
        val expnense = listOf<Expenses>(
            Expenses(1,"1",2000.toBigDecimal(),"Wedding","Wedding","SDG","2020-02-01")
        )
        expenseMonitorDataBase.expenseMonitorDao.insertExpensesForCachingData(*expnense.toTypedArray())


        //WHEN-delete expense from database by id
        val loaded = expenseMonitorDataBase.expenseMonitorDao.deleteExpenses("1")


        //THEN-delete expense
        assertThat(loaded, `is`(1))
    }


//    @Test
//    fun retrieveSumationOfMonthExpense() = runBlocking() {
//        //GIVEN-insert Expense
//        val expnense = listOf<Expenses>(
//            Expenses(1,"1",1000.toBigDecimal(),"Wedding","Wedding","SDG","2020-02-01"),
//            Expenses(2,"2",2000.toBigDecimal(),"Wedding","Wedding","SDG","2020-02-02"),
//            Expenses(3,"3",3000.toBigDecimal(),"Wedding","Wedding","SDG","2020-02-03")
//        )
//        expenseMonitorDataBase.expenseMonitorDao.insertExpensesForCachingData(*expnense.toTypedArray())
//
//
//        //WHEN-get expense from database by todayDate and currency
//        val loaded = expenseMonitorDataBase.expenseMonitorDao.retrieveSumationOfTodayExpense("2020-02-01","SDG")
//
//        //THEN-get all expenses on that day
//        assertThat(loaded, hasSize(6))
//
//    }

}

