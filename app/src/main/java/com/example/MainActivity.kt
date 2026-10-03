package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.HotelRepository
import com.example.data.db.HotelDatabase
import com.example.ui.HotelAppScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.HotelViewModel
import com.example.ui.viewmodel.HotelViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = HotelDatabase.getInstance(applicationContext)
        val repository = HotelRepository(database.bookingDao())
        val viewModelFactory = HotelViewModelFactory(repository)

        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: HotelViewModel = viewModel(factory = viewModelFactory)
                    HotelAppScreen(viewModel = viewModel)
                }
            }
        }
    }
}
