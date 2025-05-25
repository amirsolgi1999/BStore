package com.example.bstore.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.bstore.navigation.Screen
import com.example.bstore.ui.theme.back
import com.example.bstore.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel= hiltViewModel()
) {

    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    val isError by viewModel.isError.collectAsState()
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }


    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(back)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){

        TextField(
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White

            ),
            value = username,
            onValueChange = {username=it},
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clip(RoundedCornerShape(15.dp)),
            placeholder = {
                Row (
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ){

                    Text("Username")
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White

            ),
            value = password,
            onValueChange = {password=it},
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clip(RoundedCornerShape(15.dp)),
            placeholder = {
                Row (
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ){

                    Text("Password")
                }
            }
        )
        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {viewModel.login(username,password)}
        ) {
            Text("Login")
        }
        if (isError){
            Text("Username or Password is incorrect")
        }
    }
    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) navController.navigate(Screen.Home.route)
    }


}