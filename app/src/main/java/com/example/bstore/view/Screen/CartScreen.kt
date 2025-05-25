package com.example.bstore.view.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.bstore.model.cart.CartItem
import com.example.bstore.navigation.Screen
import com.example.bstore.ui.theme.back
import com.example.bstore.ui.theme.onsec
import com.example.bstore.ui.theme.sec
import com.example.bstore.view.CartItemView
import com.example.bstore.viewmodel.CartViewModel
import com.example.bstore.viewmodel.ProductViewModel


@Composable
fun CartScreen(
    navController: NavController,
    viewmodel:CartViewModel= hiltViewModel(),
    onProductClick: (Int) -> Unit = {}
){

    var showDialog by remember { mutableStateOf(false) }
    val cartList by viewmodel.cart.collectAsState()




    Box (
        modifier = Modifier
            .fillMaxSize()
            .background(back),
        contentAlignment = Alignment.TopCenter
    ){

        Column {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(Color.White)
            ){
                Text(
                    text = "Basket",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(16.dp)
                )

            }
            LazyColumn(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                items(cartList.size) { index ->
                    CartItemView (
                        item = cartList[index],
                        onRemove = { viewmodel.removeFromCart(cartList[index].id) },
                        onClick = {onProductClick(cartList[index].id)}
                    )
                }
            }
        }
        Button(
            modifier = Modifier
                .height(56.dp)
                .padding(8.dp)
                .fillMaxWidth()
                .align(Alignment.BottomEnd)
                .clip(RoundedCornerShape(10.dp)),
            onClick = {showDialog = true},

            ) {
            Text("Pay")
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Logout") },
                text = { Text("Do you want to pay?") },
                confirmButton = {
                    Button(
                        colors = ButtonDefaults.buttonColors(containerColor = onsec),
                        onClick = {
                            viewmodel.clearCart()
                            showDialog = false
                            navController.navigate(Screen.Cart.route)
                        }) { Text("pay") }
                },
                dismissButton = {
                    Button(
                        colors = ButtonDefaults.buttonColors(containerColor = onsec),
                        onClick = {
                            showDialog = false

                        }) { Text("Keep Data") }
                }

            )
        }
    }
}




