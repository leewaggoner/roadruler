package com.wreckingballsoftware.roadruler.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.wreckingballsoftware.roadruler.data.datasources.DriveSegmentsDao
import com.wreckingballsoftware.roadruler.data.datasources.DrivesDao
import com.wreckingballsoftware.roadruler.data.datasources.RoadRulerDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

private const val DATA_STORE_NAME = "com.wreckingballsoftware.roadruler"
private const val DB_NAME = "road_ruler_db"

@InstallIn(SingletonComponent::class)
@Module
object HiltModule {
    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context) : DataStore<Preferences> =
    PreferenceDataStoreFactory.create(
        corruptionHandler = ReplaceFileCorruptionHandler(produceNewData = { emptyPreferences() }),
        produceFile = { context.preferencesDataStoreFile(DATA_STORE_NAME) },
        scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    )

    @Provides
    fun provideDrivesDao(appDatabase: RoadRulerDatabase): DrivesDao {
        return appDatabase.getDrivesDao()
    }

    @Provides
    fun provideDriveSegmentsDao(appDatabase: RoadRulerDatabase): DriveSegmentsDao {
        return appDatabase.getDriveSegmentsDao()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context) =
        Room.databaseBuilder(
            context = appContext,
            klass = RoadRulerDatabase::class.java,
            name = DB_NAME,
        )
        .fallbackToDestructiveMigration()
        .build()
}