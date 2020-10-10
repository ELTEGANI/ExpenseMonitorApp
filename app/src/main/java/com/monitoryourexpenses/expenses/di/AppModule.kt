package com.monitoryourexpenses.expenses.di

import android.app.Application
import androidx.room.Room
import com.monitoryourexpenses.expenses.database.local.ExpenseMonitorDao
import com.monitoryourexpenses.expenses.database.local.ExpenseMonitorDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton


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

