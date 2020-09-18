package com.monitoryourexpenses.expenses.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal
import kotlinx.android.parcel.Parcelize
import javax.inject.Inject
import javax.inject.Singleton

@Entity(tableName = "expenses")
@Parcelize
data class Expenses(
    @PrimaryKey(autoGenerate = true)
    var expense_id: Int = 0,

    @ColumnInfo(name = "amount")
    val amount: BigDecimal?,

    @ColumnInfo(name = "description")
    var description: String?,

    @ColumnInfo(name = "expense_category")
    var expenseCategory: String?,

    @ColumnInfo(name = "currency")
    var currency: String?,

    @ColumnInfo(name = "date")
    var date: String?
) : Parcelable
