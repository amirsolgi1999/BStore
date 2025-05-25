package com.example.bstore.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.bstore.model.cart.CartItem
import com.example.bstore.ui.theme.back
import com.example.bstore.ui.theme.onsec
import com.example.bstore.ui.theme.sec

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CartItemView(
    item: CartItem,
    onRemove: () -> Unit,
    onClick: () ->Unit,
) {

    val swipeThreshold = 60.dp 
    val swipeThresholdPx = with(LocalDensity.current) { swipeThreshold.toPx() }
    val swipeState = rememberSwipeableState(initialValue = 0)
    val anchors = mapOf(
        0f to 0, 
        -swipeThresholdPx to 1 
    )


    Card (
        modifier = Modifier.clickable {onClick()},
        colors = CardDefaults.cardColors(
            containerColor = back
        )
    ){

        Box(

        ){
            Icon(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(16.dp)
                    .clickable { onRemove() },
                imageVector = Icons.Default.DeleteOutline,
                tint = onsec,
                contentDescription = ""
            )
            Row  (
                modifier = Modifier
                    .offset { IntOffset(swipeState.offset.value.toInt(), 0) }
                    .swipeable(
                        state = swipeState,
                        anchors = anchors,
                        thresholds = { _, _ -> FractionalThreshold(0.3f) },
                        orientation = Orientation.Horizontal
                    )
                    .padding( vertical = 4.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .height(110.dp)
                    .background(Color.White)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){

                Row (
                    modifier = Modifier
                        .padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    AsyncImage(
                        modifier = Modifier.size(75.dp),
                        model = item.image,
                        alpha = 1f,
                        contentDescription = "PI"
                    )
                    Column (
                        modifier = Modifier.padding(8.dp),
                        horizontalAlignment = AbsoluteAlignment.Left
                    ){
                        Text(
                            item.title.substring(0,24)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                            Text(text = item.price.toString() + "$")
                    }
                }
                
                var count by remember  { mutableStateOf(1) }
                Column  (
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(sec)
                        .width(40.dp)
                        .fillMaxHeight()
                        .padding(vertical = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ){

                    IconButton(
                        onClick = {count++},
                        modifier = Modifier
                            .background(onsec, shape = CircleShape)
                            .size(30.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Increase",
                            tint = Color.White
                        )
                    }

                    Text(
                        modifier = Modifier,
                        text = count.toString(),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
                    IconButton(
                        onClick = {if (count.equals(1)){}
                        else{count--}},
                        modifier = Modifier
                            .background(Color.White, shape = CircleShape)
                            .size(30.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "Decrease",
                            tint = Color.Black
                        )
                    }
                }
            }
        }
    }
}