package com.monitoryourexpenses.expenses.data

import com.monitoryourexpenses.expenses.api.ApiFactory
import com.monitoryourexpenses.expenses.api.ExpenseData
import com.monitoryourexpenses.expenses.api.ExpenseResponseMsg
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
}