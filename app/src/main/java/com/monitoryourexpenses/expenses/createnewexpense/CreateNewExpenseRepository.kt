package com.monitoryourexpenses.expenses.createnewexpense

import com.monitoryourexpenses.expenses.network.ApiFactory
import com.monitoryourexpenses.expenses.network.ExpenseData
import com.monitoryourexpenses.expenses.network.ExpenseResponseMsg
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn


class CreateNewExpenseRepository {
    @ExperimentalCoroutinesApi
    fun createNewExpense(expenseData: ExpenseData): Flow<ExpenseResponseMsg> {
        return flow {
            val registerationResponse = ApiFactory.expensesService.createNewExpenseAsync(expenseData)
            emit(registerationResponse)
        }.flowOn(Dispatchers.IO)
    }
}