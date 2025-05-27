package com.example.bstore.view.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bstore.ui.theme.background
import com.example.bstore.ui.theme.onsec
import com.example.bstore.viewmodel.LoginViewModel
import com.example.bstore.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    onLogout: () -> Unit,
    loginViewModel: LoginViewModel= hiltViewModel(),
    viewModel: ProfileViewModel = hiltViewModel()

) {

    var showDialog by remember { mutableStateOf(false) }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(background),
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
                        loginViewModel.logout()
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
                        loginViewModel.logout()
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