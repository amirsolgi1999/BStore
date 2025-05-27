package com.example.bstore.view.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.bstore.R
import com.example.bstore.model.category.Category
import com.example.bstore.navigation.Screen
import com.example.bstore.ui.theme.onsec
import com.example.bstore.ui.theme.sec
import com.example.bstore.viewmodel.ProductViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun Categories(
    navController: NavController,
    productViewModel: ProductViewModel = hiltViewModel()
) {
    val configuration = LocalConfiguration.current
    val isLandScape = configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE
    val message by productViewModel.message.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(message) {
        message?.let { msg ->
            launch {
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                delay(500)
                productViewModel.clearMessage()
            }
        }
    }

    Text(
        modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
        text = "Category",
        fontSize = 18.sp,
        fontWeight = FontWeight.Medium
    )

    val categories = listOf(
        Category(R.drawable.tv, "Tv", "tv"),
        Category(R.drawable.audio, "Audio", "audio"),
        Category(R.drawable.console, "Console", "gaming"),
        Category(R.drawable.laptop, "Laptop", "laptop"),
        Category(R.drawable.mobile, "Mobile", "mobile"),
        Category(R.drawable.appliance, "Utensil", "appliances")
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = 4.dp,
                horizontal = if (isLandScape) 10.dp else 16.dp
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        categories.forEach { category ->
            CategoryItem(
                icon = category.icon,
                text = category.text,
                navController = navController,
                textA = category.route
            )
        }
    }
}

@Composable
fun CategoryItem(
    icon: Int,
    text: String,
    navController: NavController,
    textA: String
) {
    val circleModifier = Modifier
        .padding(start = 12.dp)
        .size(50.dp)
        .clip(CircleShape)
        .background(sec)
        .clickable {
            navController.navigate(Screen.Category.createRoute(textA)) {
                popUpTo(Screen.Home.route) { inclusive = false }
                launchSingleTop = true
            }
        }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = circleModifier,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            androidx.compose.material.Icon(
                modifier = Modifier.size(25.dp),
                painter = painterResource(icon),
                tint = onsec,
                contentDescription = text
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = text, modifier = Modifier.padding(start = 10.dp))
    }
}


