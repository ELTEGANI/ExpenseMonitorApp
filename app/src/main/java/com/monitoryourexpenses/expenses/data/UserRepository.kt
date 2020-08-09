package com.monitoryourexpenses.expenses.data

import com.monitoryourexpenses.expenses.api.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn


class UserRepository {
    @ExperimentalCoroutinesApi
    fun registerNewUser(userData: UserData): Flow<RegisterationResponse> {
        return flow {
            val registerationResponse = ApiFactory.expensesService.createNewUser(userData)
            emit(registerationResponse)
        }.flowOn(Dispatchers.IO)
    }
}