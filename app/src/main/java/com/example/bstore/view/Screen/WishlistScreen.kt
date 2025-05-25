package com.example.bstore.view.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.bstore.model.product.Product
import com.example.bstore.model.wishlist.WishlistItem
import com.example.bstore.navigation.Screen
import com.example.bstore.ui.theme.back
import com.example.bstore.ui.theme.onsec
import com.example.bstore.ui.theme.sec
import com.example.bstore.viewmodel.ProductViewModel
import com.example.bstore.viewmodel.WishlistViewModel
import kotlinx.coroutines.launch
/*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WishlistScreen(
    onProductClick: (Int) -> Unit = {} // Default empty lambda
) {
    val viewModel: WishlistViewModel = hiltViewModel()
    val wishlist by viewModel.wishlist.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }



    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = { TopAppBar(title = { Text("Wishlist") }) }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(wishlist.size) { index ->
                WishlistItem(
                    item = wishlist[index],
                    onRemove = { viewModel.removeFromWishlist(wishlist[index].id) },
                    onAddToCart = { viewModel.addToCart(wishlist[index]) },
                    onClick = { onProductClick(wishlist[index].id) }
                )
            }
        }
    }
}

@Composable
fun WishlistItem(
    item: WishlistItem,
    onRemove: () -> Unit,
    onAddToCart: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable(onClick = onClick)
    ) {
        Column {
            Text(item.title)
            Text("$${item.price}")
            Button(onClick = onRemove) { Text("Remove") }
            Button(onClick = onAddToCart) { Text("Add to Cart") }
        }
    }
}*/



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WishlistScreen(
    navController: NavController,
    onProductClick: (Int) -> Unit = {},
    wishlistViewModel: WishlistViewModel= hiltViewModel(),
) {
    val wishlist by wishlistViewModel.wishlist.collectAsState()


    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(back)
    ){

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(Color.White)
        ){
            Text(
                text = "Wishlist",
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(16.dp)
            )

        }
        LazyVerticalGrid (
            GridCells.Fixed(3),
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 8.dp)
        ) {
            items(wishlist.size) { index ->
                WLI(
                    item = wishlist[index],
                    onRemove = { wishlistViewModel.removeFromWishlist(wishlist[index].id) },
                    onClickAdd ={wishlistViewModel.addToCart(wishlist[index])},
                    onItem = {onProductClick(wishlist[index].id)}
                )
            }

        }
    }

}


@Composable
fun WLI(
    item: WishlistItem,
    onRemove: () -> Unit,
    onClickAdd: () ->Unit,
    onItem:() -> Unit
) {


    Column (
        modifier = Modifier
            .clickable {onItem()}
            .padding(4.dp)
            .clip(RoundedCornerShape(15.dp))
            .height(230.dp)
            .fillMaxWidth()
            .background(Color.White)
    ){
        AsyncImage(
            model = item.image,
            contentDescription = "",
            modifier = Modifier
                .padding(6.dp)
                .height(95.dp)
                .fillMaxWidth()
        )

        Column (modifier = Modifier.padding(4.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
            Text(
                item.title.substring(0,12), maxLines = 1
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(item.price.toString())

        }
        Divider(modifier = Modifier.fillMaxWidth(), color = sec)

        Row (modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){

            Icon(
                modifier = Modifier.weight(0.5f)
                   .clickable { onClickAdd()}

                ,
                imageVector = Icons.Default.AddShoppingCart,
                contentDescription = "",
                tint = onsec
            )

            Icon(
                modifier = Modifier.weight(0.5f).padding(end = 2.dp).clickable { onRemove() },
                imageVector = Icons.Default.Delete,
                contentDescription = ""
            )
        }


    }

}

