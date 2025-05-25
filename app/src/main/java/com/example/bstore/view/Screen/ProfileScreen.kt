package com.example.bstore.view.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bstore.ui.theme.back
import com.example.bstore.ui.theme.onsec
import com.example.bstore.viewmodel.LoginViewModel
import com.example.bstore.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    onLogout: () -> Unit
) {

    val viewModel: ProfileViewModel = hiltViewModel()
    var showDialog by remember { mutableStateOf(false) }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(back),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

            Text("User Profile")
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = onsec),
                onClick = { showDialog = true }
            ) {
                Text("Logout")
            }

    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Logout") },
            text = { Text("Do you want to clear wishlist and cart?") },
            confirmButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = onsec),
                    onClick = {
                    viewModel.clearData()
                    showDialog = false
                    onLogout()
                }
                ) {
                    Text("Clear and Logout")
                }
            },
            dismissButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = onsec),
                    onClick = {
                    showDialog = false
                    onLogout()
                }
                ) {
                    Text("Keep Data")
                }
            }
        )
    }
}
