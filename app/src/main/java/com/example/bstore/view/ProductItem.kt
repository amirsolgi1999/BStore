package com.example.bstore.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.bstore.model.product.Product
import com.example.bstore.navigation.Screen
import com.example.bstore.ui.theme.onsec

@Composable
fun ProductItem(
    product: Product,
    navController: NavController
){

    Card (
        modifier = Modifier
            .clickable { navController.navigate("${Screen.ProductDetails.route}/${product.id}") },
    ){

        Column (
            modifier = Modifier
                .clip(RoundedCornerShape(topEnd = 10.dp, topStart = 10.dp))
                .size(120.dp,110.dp)
                .background(Color.White),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            AsyncImage(
                model = product.image,
                contentDescription = "Product Image",
                modifier = Modifier.size(100.dp)
            )
        }
        Column (
            modifier = Modifier
                .clip(RoundedCornerShape(bottomEnd = 10.dp, bottomStart = 10.dp))
                .background(Color.White)
                .size(120.dp,40.dp)
        ){
            Row (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text(
                    text = product.title.substring(0,12),
                    fontSize = 8.sp,
                    color = Color.Black,
                    maxLines = 1
                )
                Column (
                    modifier = Modifier
                        .size(35.dp,20.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(onsec),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ){
                    Text(
                        text = "${"$" + product.price.toString()}",
                        fontSize = 8.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}