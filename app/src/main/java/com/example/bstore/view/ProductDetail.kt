package com.example.bstore.view

import android.widget.Toast
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.bstore.ui.theme.background
import com.example.bstore.ui.theme.onsec
import com.example.bstore.utils.LoadingAndErrorView
import com.example.bstore.viewmodel.ProductViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ProductDetail(
    productViewModel: ProductViewModel= hiltViewModel(),
    productId:Int,
    navController: NavController,
){


    val context= LocalContext.current
    val otherProduct by productViewModel.getOtherProductByWCId(productId).collectAsState()
    val newInProduct by productViewModel.getNewInProductByIdWC(productId).collectAsState()
    val popularProduct by productViewModel.getPopularProductByIdWC(productId).collectAsState(null)
    val wishlistIds by productViewModel.wishlistIds.collectAsState()
    val cartIds by productViewModel.cartIds.collectAsState()
    val message by productViewModel.message.collectAsState()
    val isError by productViewModel.isError
    val isLoading by productViewModel.isLoading
    val popularProducts by productViewModel.popularProducts.collectAsState()
    val newInProducts by productViewModel.newInProducts.collectAsState()


    LaunchedEffect (message){
        message?.let { msg ->
            launch {
                Toast.makeText(context,msg,Toast.LENGTH_SHORT).show()
                delay(100)
                productViewModel.clearMessage()
            }
        }
    }

    LoadingAndErrorView(
        isLoading = isLoading,
        isError = isError,
        modifier = Modifier
    ) {
        when {
            popularProducts.contains(popularProduct) -> {
                Column (
                    modifier = Modifier
                        .background(background)
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
                            modifier = Modifier.fillMaxWidth().background(background)
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
                                    model = popularProduct?.image,
                                    contentDescription = "product image",
                                    modifier = Modifier.size(120.dp)
                                )
                            }
                            Column (
                                modifier = Modifier
                                    .align(alignment = Alignment.BottomEnd)
                                    .padding(end = 16.dp, top = 210.dp)
                                    .clip(RoundedCornerShape(20.dp))
                                    .size(100.dp,56.dp)
                                    .background(onsec),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ){
                                Text(
                                    text = "${popularProduct?.price.toString()}$",
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
                            .background(background)
                    ){
                        popularProduct?.title?.let {
                            Text(
                                it,
                                fontSize = 16.sp,
                                maxLines = 2
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text("Description", fontSize = 18.sp, fontWeight = FontWeight.Bold)

                        Spacer(modifier = Modifier.height(8.dp))

                        popularProduct?.description?.let {
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
                                onClick = {productViewModel.addToWishlist(popularProduct!!)}
                            ) {
                                if (wishlistIds.contains(productId)){
                                    Text("In Wish", color = onsec)
                                }else{
                                    Text("Add To Wish", fontSize = 12.sp, color = onsec)
                                }
                            }
                            Spacer(modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp))
                            Button(
                                modifier = Modifier.fillMaxWidth().weight(2f),
                                onClick = {productViewModel.addToCart(popularProduct!!)},
                                colors = if (cartIds.contains(productId))
                                    ButtonDefaults.buttonColors(containerColor = Color.Gray)
                                else
                                    ButtonDefaults.buttonColors(containerColor = onsec)
                            ) {
                                if (cartIds.contains(productId)){
                                    Text("In Cart", color = Color.White)
                                }else{
                                    Text("Add To Cart", color = Color.White)
                                }
                            }

                        }

                    }
                }

            }
            newInProducts.contains(newInProduct) ->{
                Column (
                    modifier = Modifier
                        .background(background)
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
                            modifier = Modifier.fillMaxWidth().background(background)
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
                                    model = newInProduct?.image,
                                    contentDescription = "product image",
                                    modifier = Modifier.size(120.dp)
                                )
                            }
                            Column (
                                modifier = Modifier
                                    .align(alignment = Alignment.BottomEnd)
                                    .padding(end = 16.dp, top = 210.dp)
                                    .clip(RoundedCornerShape(20.dp))
                                    .size(100.dp,56.dp)
                                    .background(onsec),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ){
                                Text(
                                    text = "${newInProduct?.price.toString()}$",
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
                            .background(background)
                    ){
                        newInProduct?.title?.let {
                            Text(
                                it,
                                fontSize = 16.sp,
                                maxLines = 2
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text("Description", fontSize = 18.sp, fontWeight = FontWeight.Bold)

                        newInProduct?.description?.let {
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
                                onClick = {productViewModel.addToWishlist(newInProduct!!)}
                            ) {
                                if (wishlistIds.contains(productId)){
                                    Text("In Wish", color = onsec)
                                }else{
                                    Text("Add To Wish", fontSize = 12.sp, color = onsec)
                                }
                            }
                            Spacer(modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp))
                            Button(
                                modifier = Modifier.fillMaxWidth().weight(2f),
                                onClick = {productViewModel.addToCart(newInProduct!!)},
                                colors = if (cartIds.contains(productId))
                                    ButtonDefaults.buttonColors(containerColor = Color.Gray)
                                else
                                    ButtonDefaults.buttonColors(containerColor = onsec)
                            ) {
                                if (cartIds.contains(productId)){
                                    Text("In Cart", color = Color.White)
                                }else{
                                    Text("Add To Cart", color = Color.White)
                                }
                            }

                        }

                    }
                }

            }
            else ->{
                Column (
                    modifier = Modifier
                        .background(background)
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
                            modifier = Modifier.fillMaxWidth().background(background)
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
                                    model = otherProduct?.image,
                                    contentDescription = "product image",
                                    modifier = Modifier.size(120.dp)
                                )
                            }
                            Column (
                                modifier = Modifier
                                    .align(alignment = Alignment.BottomEnd)
                                    .padding(end = 16.dp, top = 210.dp)
                                    .clip(RoundedCornerShape(20.dp))
                                    .size(100.dp,56.dp)
                                    .background(onsec),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ){
                                Text(
                                    text = "${otherProduct?.price.toString()}$",
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
                            .background(background)
                    ){
                        otherProduct?.title?.let {
                            Text(
                                it,
                                fontSize = 16.sp,
                                maxLines = 2
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text("Description", fontSize = 18.sp, fontWeight = FontWeight.Bold)

                        Spacer(modifier = Modifier.height(8.dp))

                        otherProduct?.description?.let {
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
                                onClick = {productViewModel.addToWishlist(otherProduct!!)}
                            ) {
                                if (wishlistIds.contains(productId)){
                                    Text("In Wish", color = onsec)
                                }else{
                                    Text("Add To Wish", fontSize = 12.sp, color = onsec)
                                }
                            }
                            Spacer(modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp))
                            Button(
                                modifier = Modifier.fillMaxWidth().weight(2f),
                                onClick = {productViewModel.addToCart(otherProduct!!)},
                                colors = if (cartIds.contains(productId))
                                    ButtonDefaults.buttonColors(containerColor = Color.Gray)
                                else
                                    ButtonDefaults.buttonColors(containerColor = onsec)
                            ) {
                                if (cartIds.contains(productId)){
                                    Text("In Cart", color = Color.White)
                                }else{
                                    Text("Add To Cart", color = Color.White)
                                }
                            }

                        }

                    }
                }
            }
        }
    }

}


