package com.example.bstore.view

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsEndWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.bstore.model.cart.CartItem
import com.example.bstore.ui.theme.back
import com.example.bstore.ui.theme.onsec
import com.example.bstore.ui.theme.sec
import com.example.bstore.viewmodel.CartViewModel
import com.example.bstore.viewmodel.ProductViewModel
import com.example.bstore.viewmodel.WishlistViewModel
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun ProductDetail(
    productViewModel: ProductViewModel= hiltViewModel(),
    productId:Int,
    navController: NavController,
    cartViewModel: CartViewModel= hiltViewModel(),
    wishlistViewModel: WishlistViewModel= hiltViewModel()
){

    val configuration = LocalConfiguration.current
    val isLandScape = configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE


    val product by productViewModel.getProductByIdWC(productId).collectAsState(null)
    val wishlistIds by productViewModel.wishlistIds.collectAsState()
    val cartIds by productViewModel.cartIds.collectAsState()
    val message by productViewModel.message.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val isError by productViewModel.isError
    val isLoading by productViewModel.isLoading



    LaunchedEffect (message){
        message?.let { msg ->
            launch {
                snackbarHostState.showSnackbar(msg)
                productViewModel.clearMessage()
            }
        }
    }


    when {
        isLoading ->{
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                CircularProgressIndicator()
            }
        }
        isError !=null ->{
            Box(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                contentAlignment = Alignment.Center
            ){
                Text(
                    text = "Your internet is down",
                    color = Color.Black,
                    fontSize = 16.sp
                )
            }
        }
        else ->{

            if (isLandScape){
                Box (
                    modifier = Modifier
                        .background(back)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                ){

                    Column {

                        Column  (
                            modifier = Modifier.background(Color.White).fillMaxWidth()
                        ){

                            Row (modifier = Modifier.background(Color.White).fillMaxWidth(),
                                verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.Start
                            ){
                                Icon(
                                    imageVector = Icons.Default.ArrowBackIosNew,
                                    contentDescription = "",
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .size(20.dp)
                                        .clickable { navController.popBackStack() }
                                )
                            }

                            Box(
                                modifier = Modifier.fillMaxWidth().background(back)
                            ){
                                Column (
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(240.dp)
                                        .background(Color.White),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ){
                                    AsyncImage(
                                        model = product?.image,
                                        contentDescription = "product image",
                                        modifier = Modifier.size(120.dp)
                                    )
                                }
                                Column (
                                    modifier = Modifier
                                        .padding(start = 790.dp, top = 210.dp)
                                        .clip(RoundedCornerShape(20.dp))
                                        .size(100.dp,56.dp)
                                        .background(onsec),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ){
                                    Text(
                                        text = "${product?.price.toString()}$",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                        Column (
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxSize()
                                .background(back)
                        ){
                            product?.title?.let {
                                Text(
                                    it,
                                    fontSize = 16.sp,
                                    maxLines = 2
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text("Description", fontSize = 18.sp, fontWeight = FontWeight.Bold)

                            product?.description?.let {
                                Text(
                                    it,
                                    maxLines = 3
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text("Buy Item", fontSize = 18.sp, fontWeight = FontWeight.Bold)

                            Row (
                                modifier = Modifier.padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ){
                                Button(
                                    modifier = Modifier.fillMaxWidth().weight(1f),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                                    onClick = {productViewModel.addToWishlist(product!!)}
                                ) {
                                    if (wishlistIds.contains(productId)){
                                        Text("In Wish")
                                    }else{
                                        Text("Add To Wish", fontSize = 12.sp)
                                    }
                                }
                                Spacer(modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp))
                                Button(
                                    modifier = Modifier.fillMaxWidth().weight(2f),
                                    onClick = {productViewModel.addToCart(product!!)},
                                    colors = if (cartIds.contains(productId))
                                        ButtonDefaults.buttonColors(containerColor = Color.Gray)
                                    else
                                        ButtonDefaults.buttonColors(containerColor = onsec)
                                ) {
                                    if (cartIds.contains(productId)){
                                        Text("In Cart", color = Color.Black)
                                    }else{
                                        Text("Add To Cart", color = Color.Black)
                                    }
                                }

                            }
                        }
                    }

                }
            }else{
                Column (
                    modifier = Modifier
                        .background(back)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                ){
                    Row (modifier = Modifier.background(Color.White).fillMaxWidth()){
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "",
                            modifier = Modifier
                                .padding(16.dp)
                                .size(20.dp)
                                .clickable { navController.popBackStack() }
                        )
                    }
                    Row (
                        modifier = Modifier.background(Color.White).fillMaxWidth()
                    ){



                        Box(
                            modifier = Modifier.fillMaxWidth().background(back)
                        ){
                            Column (
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(240.dp)
                                    .background(Color.White),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ){
                                AsyncImage(
                                    model = product?.image,
                                    contentDescription = "product image",
                                    modifier = Modifier.size(120.dp)
                                )
                            }
                            Column (
                                modifier = Modifier
                                    .padding(start = 275.dp, top = 210.dp)
                                    .clip(RoundedCornerShape(20.dp))
                                    .size(100.dp,56.dp)
                                    .background(onsec),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ){
                                Text(
                                    text = "${product?.price.toString()}$",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                    Column (
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxSize()
                            .background(back)
                    ){
                        product?.title?.let {
                            Text(
                                it,
                                fontSize = 16.sp,
                                maxLines = 2
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text("Description", fontSize = 18.sp, fontWeight = FontWeight.Bold)

                        product?.description?.let {
                            Text(
                                it,
                                maxLines = 3
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text("Buy Item", fontSize = 18.sp, fontWeight = FontWeight.Bold)

                        Row (
                            modifier = Modifier.padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ){
                            Button(
                                modifier = Modifier.fillMaxWidth().weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                                onClick = {productViewModel.addToWishlist(product!!)}
                            ) {
                                if (wishlistIds.contains(productId)){
                                    Text("In Wish")
                                }else{
                                    Text("Add To Wish", fontSize = 12.sp)
                                }
                            }
                            Spacer(modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp))
                            Button(
                                modifier = Modifier.fillMaxWidth().weight(2f),
                                onClick = {productViewModel.addToCart(product!!)},
                                colors = if (cartIds.contains(productId))
                                    ButtonDefaults.buttonColors(containerColor = Color.Gray)
                                else
                                    ButtonDefaults.buttonColors(containerColor = onsec)
                            ) {
                                if (cartIds.contains(productId)){
                                    Text("In Cart", color = Color.Black)
                                }else{
                                    Text("Add To Cart", color = Color.Black)
                                }
                            }

                        }

                    }
                }
            }

        }
    }
}



