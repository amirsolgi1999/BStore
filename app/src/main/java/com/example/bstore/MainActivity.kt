package com.example.bstore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.bstore.navigation.BStore
import com.example.bstore.ui.theme.BStoreTheme
import com.example.bstore.utils.NetworkStatusTracker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BStoreTheme {
                BStore(context = baseContext)
            }
        }
    }
}

