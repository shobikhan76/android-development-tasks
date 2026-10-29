package com.example.waterTracker


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import com.example.watertracker.ui.theme.WaterTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WaterTrackerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    WaterTrackerScreen()
                }
            }
        }
    }
}

@Composable
fun WaterTrackerScreen() {
    var glasses by remember { mutableStateOf(0) }
    val goal = 8

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Daily Water Tracker",
                style = MaterialTheme.typography.headlineMedium
            )

            Text(
                text = "$glasses / $goal glasses",
                modifier = Modifier.padding(top = 16.dp),
                style = MaterialTheme.typography.headlineSmall
            )

            Button(
                onClick = { glasses++ },
                modifier = Modifier.padding(top = 24.dp)
            ) {
                Text("Add one glass")
            }

            Button(
                onClick = { glasses = 0 },
                modifier = Modifier.padding(top = 12.dp)
            ) {
                Text("Reset")
            }
        }
    }
}