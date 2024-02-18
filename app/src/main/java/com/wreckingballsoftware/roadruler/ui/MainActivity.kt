package com.wreckingballsoftware.roadruler.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.wreckingballsoftware.roadruler.data.datasources.DataStoreWrapper
import com.wreckingballsoftware.roadruler.domain.services.ActivityTransition
import com.wreckingballsoftware.roadruler.ui.navigation.RoadRulerHost
import com.wreckingballsoftware.roadruler.ui.theme.RoadRulerTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var dataStoreWrapper: DataStoreWrapper
    @Inject
    lateinit var activityTransition: ActivityTransition

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CoroutineScope(Dispatchers.IO).launch {
            val userId =  dataStoreWrapper.getUserId("")
            if (userId.isEmpty()) {
                dataStoreWrapper.putUserId(UUID.randomUUID().toString())
            }
        }

        setContent {
            RoadRulerTheme(
                darkTheme = true,
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RoadRulerHost()
                }
            }
        }
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        activityTransition.stopTracking()
//    }
}
