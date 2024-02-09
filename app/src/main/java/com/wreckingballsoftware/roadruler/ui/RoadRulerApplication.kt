package com.wreckingballsoftware.roadruler.ui

import android.app.Application
import com.wreckingballsoftware.roadruler.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class RoadRulerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(level = Level.INFO)
            androidContext(androidContext = this@RoadRulerApplication)
            modules(modules = appModule)
        }
    }
}