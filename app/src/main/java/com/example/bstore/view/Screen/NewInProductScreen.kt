package com.example.bstore.view.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.bstore.ui.theme.back
import com.example.bstore.view.PopularProductItem
import com.example.bstore.viewmodel.ProductViewModel

@Composable
fun NewInProductScreen(
    navController: NavController,
    viewModel: ProductViewModel= hiltViewModel()
){

    val products by viewModel.newInProducts.collectAsState()
    val isLoading by viewModel.isLoading
    val isError by viewModel.isError

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(back),
        horizontalAlignment = AbsoluteAlignment.Right
    ){

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 10.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .padding(8.dp)
                    .clickable { navController.popBackStack() },
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = ""
            )
        }
        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            isError != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = isError ?: "Error",
                        color = Color.Red,
                        fontSize = 16.sp
                    )
                }

            }

            products.isEmpty() ->{
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Can't find product",
                        color = Color.Red,
                        fontSize = 16.sp
                    )
                }
            }

            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.padding(
                        top = 8.dp,
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 4.dp
                    ),
                    verticalArrangement = Arrangement.Center,
                    horizontalArrangement = Arrangement.Center
                ) {
                    items(products) { product ->
                        PopularProductItem(product, navController)
                    }
                }
            }
        }
    }
}