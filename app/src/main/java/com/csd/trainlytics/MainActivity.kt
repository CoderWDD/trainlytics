package com.csd.trainlytics

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.csd.trainlytics.core.designsystem.Surface
import com.csd.trainlytics.core.designsystem.TrainlyticsTheme
import com.csd.trainlytics.core.navigation.TrainlyticsNavGraph
import com.csd.trainlytics.ui.NavEntryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val navEntryViewModel: NavEntryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TrainlyticsTheme {
                val startDestination by navEntryViewModel.startDestination.collectAsState()
                if (startDestination == null) {
                    // Splash: transparent screen while reading DataStore
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Surface)
                    )
                } else {
                    TrainlyticsNavGraph(startDestination = startDestination!!)
                }
            }
        }
    }
}
