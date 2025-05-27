package com.example.bstore.navigation

sealed class Screen(val route: String) {
    object Home : Screen("Home")
    object Search : Screen("Search")
    object Profile : Screen("Profile")
    object Wishlist : Screen("WishList")
    object Login : Screen("Login")
    object Basket : Screen("BASKET")
    object Cart : Screen("Cart")
    object ProductIdArg : Screen("productId")
    object ProductTitleArg : Screen("productTitle")
    object PopularProductScreen : Screen("PopularProductScreen")
    object NewInProductScreen : Screen("NewInProductScreen")
    object ProductDetails : Screen("product_details")
    object ProductDetailsRoute : Screen("product_details/{productId}")
    object Category : Screen("category/{categoryName}") {
        const val CATEGORY_NAME_ARG = "categoryName"
        fun createRoute(categoryName: String) = "category/$categoryName"
    }
}

