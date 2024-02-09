package com.wreckingballsoftware.roadruler.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class RestartService : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.e("-----LEE-----", "Received broadcast: ${intent?.action ?: "null action"}")
        val serviceIntent = Intent(context, RoadRulerService::class.java)
        context?.startService(serviceIntent)
    }
}