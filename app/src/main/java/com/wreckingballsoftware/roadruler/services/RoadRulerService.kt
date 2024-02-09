package com.wreckingballsoftware.roadruler.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class RoadRulerService : Service() {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.e("-----LEE-----", "onStartCommand. Yay.")
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}