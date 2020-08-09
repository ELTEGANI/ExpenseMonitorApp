package com.monitoryourexpenses.expenses.data

import com.monitoryourexpenses.expenses.api.*
import com.monitoryourexpenses.expenses.database.Expenses
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn


class ExpensesRepository {
    @ExperimentalCoroutinesApi
    fun createNewExpense(expenseData: ExpenseData): Flow<ExpenseResponseMsg> {
        return flow {
            val registerationResponse = ApiFactory.expensesService.createNewExpense(expenseData)
            emit(registerationResponse)
        }.flowOn(Dispatchers.IO)
    }

    @ExperimentalCoroutinesApi
    fun updateExpense(expenseid:String,expenseData: ExpenseData): Flow<DeleteAndUpdateResponse> {
        return flow {
            val deleteAndUpdateResponse = ApiFactory.expensesService.updateExpense(expenseid,expenseData)
            emit(deleteAndUpdateResponse)
        }.flowOn(Dispatchers.IO)
    }


    @ExperimentalCoroutinesApi
    fun deleteExpense(expenseid:String): Flow<DeleteAndUpdateResponse> {
        return flow {
            val deleteAndUpdateResponse = ApiFactory.expensesService.deleteExpense(expenseid)
            emit(deleteAndUpdateResponse)
        }.flowOn(Dispatchers.IO)
    }

}