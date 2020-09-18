package com.monitoryourexpenses.expenses.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import javax.inject.Inject
import javax.inject.Singleton


@Entity(tableName = "categories")
data class Categories(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int?,

    @ColumnInfo(name = "CategoryName")
    var CategoryName: String?
)
