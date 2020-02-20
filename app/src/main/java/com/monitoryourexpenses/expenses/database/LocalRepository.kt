package com.monitoryourexpenses.expenses.database



class LocalRepository(private val database:ExpenseMonitorDao) {




    suspend fun getTodayExpenses(todayDate:String, currency: String): List<Expenses> {
        return database.retrieveTodayExpense(todayDate,currency)
    }



    suspend fun getWeekExpenses(startWeek:String,endWeek:String,currency: String): List<Expenses> {
        return database.retrieveWeekExpense(startWeek,endWeek,currency)
    }


    suspend fun getMonthExpenses(startMonth:String, endMonth:String, currency: String): List<Expenses> {
        return  database.retrieveMonthExpense(startMonth,endMonth,currency)
    }


//     suspend fun getSumationOfTodayExpenses(todayDate:String, currency: String): Flow<String> {
//         return database.retrieveSumationOfTodayExpense(todayDate,currency)
//     }

//
//    suspend fun getSumationOfMonthExpenses(startMonth:String, endMonth:String, currency: String){
//       database.retrieveSumationOfMonthExpense(startMonth,endMonth,currency)
//    }
//
//    suspend fun getSumationOfWeekExpenses(startWeek:String, endWeek:String, currency: String){
//        database.retrieveSumationOfWeekExpense(startWeek,endWeek,currency)
//    }
//

//
//    suspend fun clearTodayExpense(todayDate: String){
//        database.clearAllTodayExpenses(todayDate)
//    }
//
//    suspend fun clearWeekExpenses(startWeek: String,endWeek: String){
//        database.clearAllWeekExpenses(startWeek,endWeek)
//    }
//
//    suspend fun clearMonthExpenses(startMonth: String,endMonth: String){
//       database.clearAllMonthExpenses(startMonth,endMonth)
//    }
//
//
    suspend fun deleteExpneseUsingId(id:String){
        database.deleteExpenses(id)
    }


    suspend fun updateExpenseUsingId(expense_id: String,amount:String,description:String,expensecategory:String,date:String){
        database.updateExpenses(expense_id,amount,description,expensecategory,date)
    }


    suspend fun insertExpense(expenses: Expenses){
        database.insertExpenses(expenses)
    }



}