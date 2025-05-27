package com.example.bstore.view.popularProduct

import android.content.res.Configuration
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Category
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.bstore.ui.theme.background
import com.example.bstore.ui.theme.onsec
import com.example.bstore.utils.LoadingAndErrorView
import com.example.bstore.viewmodel.ProductViewModel

@Composable
fun PopularScreen(
    navController: NavController,
    viewModel: ProductViewModel= hiltViewModel()
){
    val configuration = LocalConfiguration.current
    val isLandScape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val popularProducts by viewModel.popularProducts.collectAsState()
    val selectedCategories by viewModel.selectedCategories
    var expanded by remember { mutableStateOf(false) }
    val categories = listOf("tv", "audio", "gaming", "laptop", "mobile", "appliances")
    val isLoading by viewModel.isLoading
    val isError by viewModel.isError


    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(background),
        horizontalAlignment = AbsoluteAlignment.Right
    ){
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 10.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Icon(
                modifier = Modifier
                    .padding(8.dp)
                    .clickable { navController.popBackStack() },
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = "back"
            )
            Icon(
                modifier = Modifier
                    .padding(8.dp)
                    .clickable { expanded=true },
                imageVector = Icons.Default.Category,
                contentDescription = "Category"
            )
        }

        Column (
            modifier = Modifier.padding(end = 16.dp)
        ){
            DropdownMenu(
                containerColor = Color.White,
                expanded = expanded,
                onDismissRequest = {expanded=false}
            ) {
                categories.forEach { category ->
                    Row (
                        modifier = Modifier
                            .clickable {
                                val newsSelectedCategories = if (selectedCategories.contains(category)){
                                    selectedCategories + categories
                                }else{
                                    selectedCategories - categories
                                }
                                viewModel.updatedCategories(newsSelectedCategories)
                            }
                            .padding(vertical = 2.dp, horizontal = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Checkbox(
                            colors = CheckboxColors(
                                checkedCheckmarkColor = Color.White,
                                uncheckedCheckmarkColor = Color.White,
                                checkedBoxColor = onsec,
                                uncheckedBoxColor = Color.White,
                                disabledCheckedBoxColor = Color.White,
                                disabledUncheckedBoxColor = Color.White,
                                disabledIndeterminateBoxColor = Color.Red,
                                checkedBorderColor = onsec,
                                uncheckedBorderColor = Color.Black,
                                disabledBorderColor = Color.Black,
                                disabledUncheckedBorderColor = Color.Black,
                                disabledIndeterminateBorderColor = Color.Black
                            ),
                            checked = selectedCategories.contains(category),
                            onCheckedChange = { isChecked ->
                                val newSelectedCategories = if (isChecked){
                                    selectedCategories + category
                                }else{
                                    selectedCategories - category
                                }
                                viewModel.updatedCategories(newSelectedCategories)
                            }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(category.replaceFirstChar { it.uppercase() })
                    }
                }
            }
        }

        LoadingAndErrorView(
            isLoading = isLoading,
            isError = isError,
            modifier = Modifier
        ) {
            when {
            popularProducts.isEmpty() -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "Can't find product",
                        color = Color.Black,
                        fontSize = 16.sp
                    )
                }
            }
            else -> {
                if (isLandScape) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(4),
                        modifier = Modifier.padding(start = 16.dp, bottom = 4.dp, end = 16.dp, top = 8.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        items(popularProducts) { product ->
                            PopularProductItem(product, navController)
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.padding(start = 16.dp,top=8.dp, end = 16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        items(popularProducts) { product ->
                            PopularProductItem(product, navController)
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
        }
    }
}