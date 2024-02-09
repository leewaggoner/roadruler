package com.wreckingballsoftware.roadruler.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.wreckingballsoftware.roadruler.data.ActivityTransitionProvider
import com.wreckingballsoftware.roadruler.ui.theme.RoadRulerTheme

class MainActivity : ComponentActivity() {
    private var needsPermission = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            RoadRulerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //ask for permission
                    ActivityTransitionProvider.startTracking(
                        context = applicationContext,
                        needPermission = {
                            needsPermission = true
                        },
                        onFailure = { },
                    )
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RoadRulerTheme {
        Greeting("Android")
    }
}