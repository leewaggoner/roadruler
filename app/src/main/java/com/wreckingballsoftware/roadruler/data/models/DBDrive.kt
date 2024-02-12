package com.wreckingballsoftware.roadruler.data.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.UUID

const val INVALID_DB_ID = 0L

@Parcelize
@Entity(
    indices = [Index(value = ["drive_id"], unique = true)],
    tableName = "drives",
)
data class DBDrive(
    @PrimaryKey(autoGenerate = true)
    val id: Long = INVALID_DB_ID,
    @ColumnInfo(name = "user_id")
    val userId: String,
    @ColumnInfo(name = "drive_id")
    val driveId: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "datetime_created")
    val dateTimeCreated: String,
) : Parcelable
