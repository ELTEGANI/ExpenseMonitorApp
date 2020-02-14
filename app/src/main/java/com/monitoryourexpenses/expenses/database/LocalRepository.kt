package com.monitoryourexpenses.expenses.database



class LocalRepository(private val database:ExpenseMonitorDao) {


    suspend fun getMonthExpenses(startMonth:String, endMonth:String, currency: String){
        database.retrieveMonthExpense(startMonth,endMonth,currency)
    }

    suspend fun getWeekExpenses(startWeek:String, endWeek:String, currency: String){
        database.retrieveWeekExpense(startWeek,endWeek,currency)
    }

    suspend fun getTodayExpenses(todayDate:String, currency: String){
        database.retrieveTodayExpense(todayDate,currency)
    }

    suspend fun getSumationOfMonthExpenses(startMonth:String, endMonth:String, currency: String){
       database.retrieveSumationOfMonthExpense(startMonth,endMonth,currency)
    }

    suspend fun getSumationOfWeekExpenses(startWeek:String, endWeek:String, currency: String){
        database.retrieveSumationOfWeekExpense(startWeek,endWeek,currency)
    }

    suspend fun getSumationOfTodayExpenses(todayDate:String, currency: String){
        database.retrieveSumationOfTodayExpense(todayDate,currency)
    }

    suspend fun clearTodayExpense(todayDate: String){
        database.clearAllTodayExpenses(todayDate)
    }

    suspend fun clearWeekExpenses(startWeek: String,endWeek: String){
        database.clearAllWeekExpenses(startWeek,endWeek)
    }

    suspend fun clearMonthExpenses(startMonth: String,endMonth: String){
       database.clearAllMonthExpenses(startMonth,endMonth)
    }


    suspend fun deleteExpnese(id:String){
        database.deleteExpenses(id)
    }

    suspend fun updateExpense(id: String){
        database.updateExpenses(id)
    }


    suspend fun insertExpense(expenses: Expenses){
        database.insertExpenses(expenses)
    }



}