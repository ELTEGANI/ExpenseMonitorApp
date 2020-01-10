package com.expensemoitor.expensemonitor

import androidx.room.Room
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.expensemoitor.expensemonitor.database.ExpenseMonitorDao
import com.expensemoitor.expensemonitor.database.ExpenseMonitorDataBase
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class ExpenseMonitorDataBaseTest {

    private lateinit var expenseMonitorDao: ExpenseMonitorDao
    private lateinit var db: ExpenseMonitorDataBase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, ExpenseMonitorDataBase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        expenseMonitorDao = db.expenseMonitorDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

//    @Test
//    @Throws(Exception::class)
//    fun insert() {
//        val userExpenses = UserExpenses(currency = "SDG",todayExpenses = BigDecimal("1000"),
//            weekExpenses = BigDecimal("2000"),monthExpenses = BigDecimal("3000")
//        )
//        expenseMonitorDao.insertExpense(userExpenses)
//        val currencyName = expenseMonitorDao.checkCurrencyExistence("SDG")
//        assertEquals(currencyName,"SDG")
//    }
}