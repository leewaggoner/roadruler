package com.wreckingballsoftware.roadruler.data.services

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.ActivityRecognition
import com.google.android.gms.location.ActivityTransition
import com.google.android.gms.location.ActivityTransitionRequest
import com.google.android.gms.location.DetectedActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ActionTransition @Inject constructor(
    @ApplicationContext val context: Context,
) {
    private val _transition = MutableStateFlow("UNKNOWN")
    val transition: StateFlow<String> = _transition
    private var pendingIntent: PendingIntent? = null

    @SuppressWarnings("MissingPermission")
    fun startTracking(
        onSuccess: () -> Unit = { },
        onFailure: (String) -> Unit = { }
    ) {
        pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            Intent(context, ActionTransitionReceiver::class.java),
            PendingIntent.FLAG_MUTABLE)

        val request = ActivityTransitionRequest(getTransitions())

        if (permissionGranted()) {
            pendingIntent?.let { trackingIntent ->
                ActivityRecognition.getClient(context)
                    .requestActivityTransitionUpdates(request, trackingIntent)
                    .addOnSuccessListener {
                        Log.d(
                            "--- ${ActivityTransition::class.simpleName}",
                            "Activity recognition started"
                        )
                        onSuccess()
                    }
                    .addOnFailureListener { exception ->
                        val message =
                            "Activity recognition could not be started: ${exception.message}"
                        Log.d("--- ${ActionTransition::class.simpleName}", message)
                        onFailure(message)
                    }
            }
        }
    }

    @SuppressWarnings("MissingPermission")
    fun stopTracking() {
        if (permissionGranted()) {
            pendingIntent?.let { trackingIntent ->
                ActivityRecognition.getClient(context).removeActivityTransitionUpdates(trackingIntent)
                    .addOnSuccessListener {
                        trackingIntent.cancel()
                        Log.d("--- ${ActivityTransition::class.simpleName}", "Activity recognition canceled")
                    }
                    .addOnFailureListener { exception ->
                        val message = "Activity recognition could not be canceled: ${exception.message}"
                        Log.e("--- ${ActivityTransition::class.simpleName}", message)
                    }
            }
        }
    }

    fun onDetectedTransitionEvent(event: String) {
        _transition.value = event
    }

    private fun permissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACTIVITY_RECOGNITION
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    private fun getTransitions() = listOf(
        ActivityTransition.Builder()
            .setActivityType(DetectedActivity.STILL)
            .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
            .build(),
        ActivityTransition.Builder()
            .setActivityType(DetectedActivity.STILL)
            .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
            .build(),
        ActivityTransition.Builder()
            .setActivityType(DetectedActivity.WALKING)
            .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
            .build(),
        ActivityTransition.Builder()
            .setActivityType(DetectedActivity.WALKING)
            .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
            .build(),
        ActivityTransition.Builder()
            .setActivityType(DetectedActivity.IN_VEHICLE)
            .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
            .build(),
        ActivityTransition.Builder()
            .setActivityType(DetectedActivity.IN_VEHICLE)
            .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
            .build(),
        ActivityTransition.Builder()
            .setActivityType(DetectedActivity.ON_BICYCLE)
            .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
            .build(),
        ActivityTransition.Builder()
            .setActivityType(DetectedActivity.ON_BICYCLE)
            .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
            .build(),
        ActivityTransition.Builder()
            .setActivityType(DetectedActivity.RUNNING)
            .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
            .build(),
        ActivityTransition.Builder()
            .setActivityType(DetectedActivity.RUNNING)
            .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
            .build(),
    )
}
