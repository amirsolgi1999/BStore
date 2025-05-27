package com.example.bstore.view.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.bstore.model.product.Product
import com.example.bstore.ui.theme.onsec
import com.example.bstore.view.ProductItem

@Composable
fun ProductSection(
    title:String,
    products:List<Product>,
    navController: NavController,
    destination: String
){

    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Normal
        )
        Text(
            "See All",
            fontSize = 14.sp,
            color = onsec,
            modifier = Modifier.clickable { navController.navigate(destination) }
        )
    }
    LazyRow (
        modifier = Modifier
            .padding(start = 16.dp, top = 8.dp, bottom =8.dp)
    ){
        items(products){product ->
            ProductItem( product,navController)
            Spacer(modifier = Modifier.width(8.dp))

        }

    }
}

