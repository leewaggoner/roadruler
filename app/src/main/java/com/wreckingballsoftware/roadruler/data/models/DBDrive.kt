package com.wreckingballsoftware.roadruler.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

const val INVALID_DB_ID = 0L

@Entity(tableName = "drives")
data class DBDrive(
    @PrimaryKey(autoGenerate = true)
    val id: Long = INVALID_DB_ID,
    @ColumnInfo(name = "user_id")
    val userId: String = "",
    @ColumnInfo(name = "datetime_created")
    val dateTimeCreated: String = "",
)
