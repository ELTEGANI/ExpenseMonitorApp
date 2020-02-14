package com.monitoryourexpenses.expenses.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal


@Entity(tableName = "expenses")
data class Expenses (
    @PrimaryKey
    @ColumnInfo(name = "expense_id")
    val expense_id: String,

    @ColumnInfo(name = "amount")
    val amount: BigDecimal,

    @ColumnInfo(name = "description")
    var description: String,

    @ColumnInfo(name = "expense_category")
    var expenseCategory: String,

    @ColumnInfo(name = "currency")
    var currency: String,

    @ColumnInfo(name = "date")
    var date: String
)