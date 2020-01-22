package com.monitoryourexpenses.expenses.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal


@Entity(tableName = "user_expense_table")
data class UserExpenses(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "today_expenses",defaultValue = "0")
    val todayExpenses: BigDecimal ,

    @ColumnInfo(name = "week_expenses",defaultValue = "0")
    var weekExpenses:  BigDecimal,

    @ColumnInfo(name = "month_expenses",defaultValue = "0")
    var monthExpenses: BigDecimal ,

    @ColumnInfo(name = "currency")
    var currency: String
)