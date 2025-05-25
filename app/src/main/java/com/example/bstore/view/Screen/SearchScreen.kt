package com.example.bstore.view.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.bstore.NetworkStatusTracker
import com.example.bstore.R
import com.example.bstore.model.product.Product
import com.example.bstore.ui.theme.back
import com.example.bstore.ui.theme.onsec
import com.example.bstore.view.PopularProductItem
import com.example.bstore.view.SearchBar
import com.example.bstore.view.TopBarA
import com.example.bstore.viewmodel.ProductViewModel


@Composable
fun SearchScreen(
    viewModel: ProductViewModel= hiltViewModel(),
    navController: NavController,
    networkStatusTracker: NetworkStatusTracker
){


    val searchQuery by viewModel.searchProduct
    val filterProducts by viewModel.filteredProducts

    val isConnected by networkStatusTracker.isConnected.observeAsState(initial = networkStatusTracker.isNetworkAvailable())
    var showRetry by remember { mutableStateOf(false) }

    LaunchedEffect(isConnected) {
        if (isConnected) {
            viewModel.products
            showRetry = false
        } else {
            showRetry = true
        }
    }
    when{
        !isConnected ->{
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Your internet is down!")
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { viewModel.products },
                    colors = ButtonColors(
                        containerColor = onsec,
                        contentColor = Color.White,
                        disabledContainerColor = onsec,
                        disabledContentColor = Color.White
                    )
                ) {
                    Text("Retry")
                }
            }
        }
        else ->{

            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .background(back)
            ){
                TopBarA(
                    iconStart =null,
                    textStart = "B Store",
                    iconEnd = null,
                    onClickStart = {navController.popBackStack()}
                ) { }

                SearchBar(
                    query = searchQuery,
                    onQueryChange = viewModel::onSearchQueryChange
                )

                if (searchQuery.isNotEmpty()){
                    SearchResponse(
                        products = filterProducts,
                        navController = navController
                    )

                }else{
                    Column (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ){
                        AsyncImage(
                            model = R.drawable.sorry,
                            contentDescription = "sorry"
                        )

                        Text("Search Product")
                    }
                }
            }
        }
    }

}

@Composable
fun SearchResponse(
    products:List<Product>,
    navController: NavController
){
    val configuration = LocalConfiguration.current
    val isLandScape = configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE


    if (products.isEmpty()){

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ){
            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                AsyncImage(
                    model = R.drawable.dd,
                    contentDescription = "",
                    modifier = Modifier.size(200.dp)
                )
                Text("Sorry This product is not available!")
                Text("Search for other product!")
            }
        }
    }else{
        if (isLandScape){
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier.padding(bottom = 4.dp, start = 16.dp, end = 16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalArrangement = Arrangement.Center
            ) {
                items(products){product ->
                    PopularProductItem(product,navController)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }else{
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.padding(bottom = 4.dp, start = 16.dp, end = 16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalArrangement = Arrangement.Center
            ) {
                items(products){product ->
                    PopularProductItem(product,navController)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

    }
}