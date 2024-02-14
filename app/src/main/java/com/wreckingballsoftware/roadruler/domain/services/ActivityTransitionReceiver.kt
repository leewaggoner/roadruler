package com.wreckingballsoftware.roadruler.domain.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.ActivityTransition
import com.google.android.gms.location.ActivityTransitionEvent
import com.google.android.gms.location.ActivityTransitionResult
import com.google.android.gms.location.DetectedActivity

const val TRANSITION = "transition"

class ActivityTransitionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("-----LEE-----", "ActivityTransitionReceiver: onReceive")
        intent?.let { arIntent ->
            if (ActivityTransitionResult.hasResult(intent)) {
                val result = ActivityTransitionResult.extractResult(arIntent)
                if (result != null && context != null) {
                    processTransitionResults(context, result.transitionEvents)
                }
            }
        }
    }

    private fun processTransitionResults(context: Context, transitionEvents: List<ActivityTransitionEvent>) {
        //if driving, start location service
        for (event in transitionEvents) {
            val transition = "${mapTransitionToString(event)} ${mapActivityToString(event)}"
            val intent = Intent(context, MileageService::class.java)
            Log.d("-----LEE-----", "ActivityTransitionReceiver: $transition")
            intent.putExtra(TRANSITION, transition)
            context.startForegroundService(intent)
        }
    }

    private fun mapActivityToString(event: ActivityTransitionEvent) =
        when (event.activityType) {
            DetectedActivity.STILL -> "STILL"
            DetectedActivity.ON_FOOT -> "ON_FOOT"
            DetectedActivity.WALKING -> "WALKING"
            DetectedActivity.RUNNING -> "RUNNING"
            DetectedActivity.ON_BICYCLE -> "ON_BICYCLE"
            DetectedActivity.IN_VEHICLE -> "IN_VEHICLE"
            else -> "UNKNOWN"
        }

    private fun mapTransitionToString(event: ActivityTransitionEvent) =
        when (event.transitionType) {
            ActivityTransition.ACTIVITY_TRANSITION_ENTER -> "ENTER"
            ActivityTransition.ACTIVITY_TRANSITION_EXIT -> "EXIT"
            else -> "UNKNOWN"
        }
}