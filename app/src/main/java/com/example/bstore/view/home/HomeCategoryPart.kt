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
import com.example.bstore.utils.NetworkStatusTracker
import com.example.bstore.R
import com.example.bstore.navigation.Screen
import com.example.bstore.ui.theme.onsec
import com.example.bstore.ui.theme.sec
import com.example.bstore.viewmodel.ProductViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun Categories(
    navController: NavController,
    productViewModel: ProductViewModel= hiltViewModel()
) {

    val configuration = LocalConfiguration.current
    val isLandScape = configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE
    val message by productViewModel.message.collectAsState()
    val context = LocalContext.current


    LaunchedEffect (message){
        message?.let { msg ->
            launch {
                Toast.makeText(context,msg,Toast.LENGTH_LONG).show()
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

    if (isLandScape){
        Row(
            modifier = Modifier
                .padding(vertical = 4.dp, horizontal = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CategoryItem(
                icon = R.drawable.tv,
                text = "Tv",
                navController = navController,
                textA = "tv",
            )
            CategoryItem(
                icon = R.drawable.audio,
                text = "Audio",
                navController = navController,
                textA = "audio",
            )
            CategoryItem(
                icon = R.drawable.console,
                text = "Console",
                navController = navController,
                textA = "gaming",
            )
            CategoryItem(
                icon = R.drawable.laptop,
                text = "Laptop",
                navController = navController,
                textA = "laptop",

            )
            CategoryItem(
                icon = R.drawable.mobile,
                text = "Mobile",
                navController = navController,
                textA = "mobile",
            )
            CategoryItem(
                icon = R.drawable.appliance,
                text = "Utensil",
                navController = navController,
                textA = "appliances",
            )
        }
    }else{
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp, bottom = 4.dp, end = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CategoryItem(
                icon = R.drawable.tv,
                text = "Tv",
                navController = navController,
                textA = "tv",
            )
            CategoryItem(
                icon = R.drawable.audio,
                text = "Audio",
                navController = navController,
                textA = "audio",
            )
            CategoryItem(
                icon = R.drawable.console,
                text = "Console",
                navController = navController,
                textA = "gaming",

            )
            CategoryItem(
                icon = R.drawable.laptop,
                text = "Laptop",
                navController = navController,
                textA = "laptop",
            )
            CategoryItem(
                icon = R.drawable.mobile,
                text = "Mobile",
                navController = navController,
                textA = "mobile",

            )
            CategoryItem(
                icon = R.drawable.appliance,
                text = "Utensil",
                navController = navController,
                textA = "appliances",
            )
        }
    }

}

@Composable
fun CategoryItem(
    icon: Int,
    text: String,
    navController: NavController,
    textA:String,
) {


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier
                .padding(start = 12.dp)
                .size(50.dp)
                .clip(CircleShape)
                .background(sec)
                .clickable {

                    navController.navigate(
                            Screen.Category.createRoute(textA)
                        ) {
                            popUpTo(Screen.Home.route) { inclusive = false }
                            launchSingleTop = true
                        }

                           },
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
