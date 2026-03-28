package com.csd.trainlytics

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.csd.trainlytics.core.designsystem.TrainlyticsTheme
import com.csd.trainlytics.core.navigation.TrainlyticsNavGraph
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TrainlyticsTheme {
                TrainlyticsNavGraph()
            }
        }
    }
}
