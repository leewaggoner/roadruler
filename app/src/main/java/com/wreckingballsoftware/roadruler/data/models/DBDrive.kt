package com.wreckingballsoftware.roadruler.data.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

const val INVALID_DB_ID = 0L

@Parcelize
@Entity(
    tableName = "drives",
)
data class DBDrive(
    @PrimaryKey(autoGenerate = true)
    val id: Long = INVALID_DB_ID,
    @ColumnInfo(name = "user_id")
    val userId: String = "",
    @ColumnInfo(name = "datetime_created")
    val dateTimeCreated: String = "",
) : Parcelable
