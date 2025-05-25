package com.example.bstore.navigation

import android.content.Context
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.EmojiPeople
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCartCheckout
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.bstore.NetworkStatusTracker
import com.example.bstore.navigation.Screen.ProductDetails
import com.example.bstore.ui.theme.back
import com.example.bstore.ui.theme.onsec
import com.example.bstore.view.LoginScreen
import com.example.bstore.view.ProductDetail
import com.example.bstore.view.Screen.CategoryScreen
import com.example.bstore.view.Screen.HomeScreen
import com.example.bstore.view.Screen.NewInProductScreen
import com.example.bstore.view.Screen.PopularScreen
import com.example.bstore.view.Screen.CartScreen
import com.example.bstore.view.Screen.ProfileScreen
import com.example.bstore.view.Screen.SearchScreen
import com.example.bstore.view.Screen.WishlistScreen
import com.example.bstore.viewmodel.ProductViewModel



@Composable
fun BStore(
    context: Context,
    viewModel: ProductViewModel = hiltViewModel()
){

    val navController = rememberNavController()

    val routesWithBottomBar= listOf(
        Screen.Home.route,
        Screen.Search.route,
        Screen.Wishlist.route,
        Screen.Basket.route,
        Screen.Profile.route,
        Screen.PopularProductScreen.route,
        Screen.NewInProductScreen.route,
        Screen.Category.route,
        Screen.Cart.route
    )

    val items = listOf(
        Screen.Home,
        Screen.Search,
        Screen.Wishlist,
        Screen.Cart,
        Screen.Profile

    )



    Scaffold (
        bottomBar = {
           val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

            if (currentRoute in routesWithBottomBar ) {
                NavigationBar(
                    tonalElevation = 4.dp,
                    containerColor = Color.White
                ) {

                    items.forEach { screen ->
                        NavigationBarItem(
                            colors = NavigationBarItemColors(
                                selectedIconColor = onsec,
                                selectedTextColor = Color.Black,
                                selectedIndicatorColor = back,
                                unselectedIconColor = Color.Black,
                                unselectedTextColor = Color.Black,
                                disabledIconColor = Color.Black,
                                disabledTextColor = Color.Black
                            ),
                            icon = {
                                Icon(
                                    imageVector = when (screen) {
                                        Screen.Cart -> Icons.Default.ShoppingCartCheckout
                                        Screen.Home->Icons.Default.Home
                                        Screen.Search -> Icons.Default.Search
                                        Screen.Wishlist -> Icons.Default.Bookmark
                                        Screen.Profile -> Icons.Default.ManageAccounts
                                        else -> Icons.Default.EmojiPeople

                                    },
                                    contentDescription = screen.route
                                )
                            },
                            label = { Text(screen.route) },
                            selected = currentRoute == screen.route,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = false
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding),
        ) {

            composable(Screen.Login.route) {
                LoginScreen(navController)
            }

            composable(Screen.Home.route) {
                HomeScreen(
                    navController = navController,
                    networkStatusTracker = NetworkStatusTracker(context),
                )
            }

            composable(Screen.Profile.route) {
                ProfileScreen(
                    onLogout = {navController.navigate(Screen.Login.route)}
                )
            }

            composable(
                route = Screen.Cart.route
            ) {
                CartScreen(navController,
                    onProductClick = {  productId ->
                        navController.navigate("${ProductDetails.route}/$productId")
                    })
            }

            composable(Screen.Wishlist.route) { bakStackEntry ->
              //  val productI = bakStackEntry.arguments?.getInt(Screen.PRODUCT_ID_ARG.route) ?: 0

                WishlistScreen(
                    onProductClick = { productId ->
                        navController.navigate("${ProductDetails.route}/$productId")
                    },
                    navController = navController,

                )
            }

            composable(

                route = Screen.Search.route
            ) {
                SearchScreen(
                    navController = navController,
                    networkStatusTracker = NetworkStatusTracker(context)
                )
            }

            composable(
                route = Screen.Category.route,
                arguments = listOf(navArgument(Screen.Category.CATEGORY_NAME_ARG) {
                    type = NavType.StringType
                })
            ) { backStackEntry ->
                val categoryName =
                    backStackEntry.arguments?.getString(Screen.Category.CATEGORY_NAME_ARG) ?: ""
                CategoryScreen(viewModel, navController, categoryName)
            }

            composable(Screen.PopularProductScreen.route) {
                PopularScreen(
                    navController = navController,
                )
            }

            composable(Screen.NewInProductScreen.route) {
                NewInProductScreen(navController)
            }

            composable(
                route = Screen.ProductDetailsRoute.route,
                arguments = listOf(navArgument(Screen.ProductIdArg.route) {
                    type = NavType.IntType
                })
            ) { backStackEntry ->
                val productId = backStackEntry.arguments?.getInt(Screen.ProductIdArg.route) ?: 0

                productId?.let {
                    ProductDetail(
                        productId = productId,
                        navController = navController,
                    )
                }
            }

        }
    }
}