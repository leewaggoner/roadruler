package com.wreckingballsoftware.roadruler.data.repos

import android.content.Context
import com.wreckingballsoftware.roadruler.data.ActivityTransition

class ActivityTransitionRepo(
    private val context: Context
) {
    var transition = "Still"

    fun startTracking(
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        ActivityTransition.startTracking(
            context = context,
            onSuccess = {
                onSuccess()
            },
            onFailure = {
                onFailure(it)
            }
        )
    }
}