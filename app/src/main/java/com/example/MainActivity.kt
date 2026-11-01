package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.ui.FitPulseApp
import com.example.ui.theme.FitPulseTheme
import com.example.viewmodel.FitnessViewModel

class MainActivity : ComponentActivity() {
  private val viewModel: FitnessViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      FitPulseTheme {
        FitPulseApp(viewModel = viewModel)
      }
    }
  }
}
