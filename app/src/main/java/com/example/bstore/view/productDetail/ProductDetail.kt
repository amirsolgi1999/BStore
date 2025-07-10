package com.example.bstore.view.productDetail

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
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
                popularProduct?.let {
                    ProductScreen(
                        product = it,
                        navController = navController,
                        productId = productId
                    )
                }

            }
            newInProducts.contains(newInProduct) -> {
                newInProduct?.let {
                    ProductScreen(
                        product = it,
                        navController = navController,
                        productId = productId
                    )
                }

            }

            else ->{
                otherProduct?.let {
                    ProductScreen(
                        product = it,
                        navController = navController,
                        productId = productId
                    )
                }

            }
        }
    }

}


