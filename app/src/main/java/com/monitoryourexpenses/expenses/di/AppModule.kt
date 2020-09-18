package com.monitoryourexpenses.expenses.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.monitoryourexpenses.expenses.database.Categories
import com.monitoryourexpenses.expenses.database.ExpenseMonitorDao
import com.monitoryourexpenses.expenses.database.ExpenseMonitorDataBase
import com.monitoryourexpenses.expenses.database.Expenses
import com.monitoryourexpenses.expenses.prefs.ExpenseMonitorSharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton
import kotlin.math.exp


@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(application: Application): ExpenseMonitorDataBase {
        return Room
            .databaseBuilder(application, ExpenseMonitorDataBase::class.java, "expense_monitor_database.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideExpenseMonitorDao(appDatabase: ExpenseMonitorDataBase): ExpenseMonitorDao {
        return appDatabase.expenseMonitorDao
    }



}

