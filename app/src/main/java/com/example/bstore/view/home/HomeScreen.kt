package com.example.bstore.view.home

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCartCheckout
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.bstore.utils.NetworkStatusTracker
import com.example.bstore.R
import com.example.bstore.model.product.Product
import com.example.bstore.navigation.Screen
import com.example.bstore.ui.theme.background
import com.example.bstore.ui.theme.onsec
import com.example.bstore.utils.LoadingAndErrorView
import com.example.bstore.viewmodel.ProductViewModel


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    viewModel: ProductViewModel= hiltViewModel(),
    navController: NavController,
    networkStatusTracker: NetworkStatusTracker
    ){

    val newProduct by viewModel.newInProducts.collectAsState()
    val products by viewModel.popularProducts.collectAsState()
    val searchQuery by viewModel.searchProduct
    val filterProducts by viewModel.filteredPopularProducts
    val filterNewProducts by viewModel.filteredNewProducts
    val isError by viewModel.isError
    val isLoading by viewModel.isLoading

    val isConnected by networkStatusTracker.isConnected.observeAsState(initial = networkStatusTracker.isNetworkAvailable())
    var showRetry by remember { mutableStateOf(false) }

    LaunchedEffect(isConnected) {
        if (isConnected) {
            viewModel.popularProducts
            showRetry = false
        } else {
            showRetry = true
        }
    }

    LoadingAndErrorView(
        isLoading = isLoading,
        isError = isError,
        modifier = Modifier
    ) {
        when {
            !isConnected ->{
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Your internet is down!")
                    Text("Check your internet connection!")
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { navController.navigate(Screen.Home.route) },
                        colors = ButtonColors(
                            containerColor = onsec,
                            contentColor = Color.White,
                            disabledContainerColor = onsec,
                            disabledContentColor = Color.White
                        )) {
                        Text("Retry")
                    }
                }
            }
            else ->{
                Column (
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .background(background)
                ){
                    TopBarA(
                        iconStart = null,
                        textStart = "B Store",
                        iconEnd = Icons.Default.ShoppingCartCheckout,
                        onClickStart = {navController.popBackStack()},
                        onClickEnd = {navController.navigate(Screen.Cart.route)}
                    )

                    SearchBar(
                        query = searchQuery,
                        onQueryChange = viewModel::onSearchQueryChange
                    )
                    if (searchQuery.isNotEmpty()){
                        SearchResult(
                            products = filterProducts,
                            newProducts = filterNewProducts,
                            navController
                        )
                    }else{
                        MainContent(
                            products = products,
                            newProducts = newProduct,
                            navController,
                        )
                    }
                }
            }
        }

    }
}

@Composable
fun MainContent(
    products:List<Product>,
    newProducts:List<Product>,
    navController: NavController,
) {

    Column {

        Box(
            modifier = Modifier
                .clickable {   navController.navigate(Screen.PopularProductScreen.route) }
                .padding(horizontal = 16.dp)
                .height(210.dp)
                .fillMaxWidth()
        ) {
            AsyncImage(
                modifier = Modifier
                    .clip(RoundedCornerShape(15.dp))
                    .fillMaxSize()
                    .aspectRatio(1f),
                model = R.drawable.frame,
                contentDescription = "ss"
            )
        }
        Categories(navController = navController)

        ProductSection(
            title = "Popular",
            products = products,
            navController = navController,
            destination = Screen.PopularProductScreen.route
        )
        ProductSection(
            title = "New In",
            products = newProducts,
            navController = navController,
            destination = Screen.NewInProductScreen.route
        )
    }
}















