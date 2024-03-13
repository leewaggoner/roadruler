package com.wreckingballsoftware.roadruler.data.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.ActivityTransition
import com.google.android.gms.location.ActivityTransitionEvent
import com.google.android.gms.location.ActivityTransitionResult
import com.google.android.gms.location.DetectedActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

const val START_TRACKING_MILES = "start_tracking_miles"

@AndroidEntryPoint
class ActionTransitionReceiver : BroadcastReceiver() {
    @Inject
    lateinit var actionTransition: ActionTransition
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("--- ${ActionTransitionReceiver::class.simpleName}", "onReceive")
        intent?.let { atIntent ->
            if (ActivityTransitionResult.hasResult(intent)) {
                val result = ActivityTransitionResult.extractResult(atIntent)
                if (result != null && context != null) {
                    processTransitionResults(context, result.transitionEvents)
                }
            }
        }
    }

    private fun processTransitionResults(context: Context, transitionEvents: List<ActivityTransitionEvent>) {
        //if driving, start location service
        for (event in transitionEvents) {
            val uiTransition = mapActivityToString(event)
            val transition = "${mapTransitionToString(event)} $uiTransition"
            Log.d("--- ${ActionTransitionReceiver::class.simpleName}", "Transition: $transition")
            actionTransition.onDetectedTransitionEvent(uiTransition)

            if (event.activityType == DetectedActivity.IN_VEHICLE) {
                val intent = Intent(context, MileageService::class.java)
                when (event.transitionType) {
                    ActivityTransition.ACTIVITY_TRANSITION_ENTER -> {
                        intent.putExtra(START_TRACKING_MILES, true)
                    }
                    ActivityTransition.ACTIVITY_TRANSITION_EXIT -> {
                        intent.putExtra(START_TRACKING_MILES, false)
                    }
                }
                context.startForegroundService(intent)
            }
        }
    }

    private fun mapActivityToString(event: ActivityTransitionEvent) =
        when (event.activityType) {
            DetectedActivity.STILL -> "STILL"
            DetectedActivity.ON_FOOT -> "STAND"
            DetectedActivity.WALKING -> "WALK"
            DetectedActivity.RUNNING -> "RUN"
            DetectedActivity.ON_BICYCLE -> "BIKE"
            DetectedActivity.IN_VEHICLE -> "DRIVE"
            else -> "UNKNOWN"
        }

    private fun mapTransitionToString(event: ActivityTransitionEvent) =
        when (event.transitionType) {
            ActivityTransition.ACTIVITY_TRANSITION_ENTER -> "ENTER"
            ActivityTransition.ACTIVITY_TRANSITION_EXIT -> "EXIT"
            else -> "UNKNOWN"
        }
}