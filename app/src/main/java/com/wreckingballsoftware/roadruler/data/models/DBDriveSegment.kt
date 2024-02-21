package com.wreckingballsoftware.roadruler.data.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    tableName = "drive_segments",
    indices = [Index(value = ["drive_id"])],
    foreignKeys = [
        ForeignKey(
            entity = DBDrive::class,
            parentColumns = ["id"],
            childColumns = ["drive_id"],
            onDelete = ForeignKey.CASCADE,
        )
    ]
)
data class DBDriveSegment(
    @PrimaryKey(autoGenerate = true)
    val id: Long = INVALID_DB_ID,
    @ColumnInfo(name = "drive_id")
    val driveId: Long,
    @ColumnInfo(name = "latitude")
    val latitude: String,
    @ColumnInfo(name = "longitude")
    val longitude: String,
    @ColumnInfo(name = "datetime_created")
    val dateTimeCreated: String,
) : Parcelable
