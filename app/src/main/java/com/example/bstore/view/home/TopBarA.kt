package com.example.bstore.view.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TopBarA(
    iconStart:ImageVector?=null,
    textStart:String?=null,
    iconEnd:ImageVector?=null,
    onClickStart: () -> Boolean,
    onClickEnd: () ->Unit,

    ){

    Row (
        modifier = Modifier
            .background(Color.White)
            .height(56.dp)
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        if (textStart != null) {
            Text(
                text = textStart,
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
        }
        if (iconStart != null) {
            IconButton (
                onClick = {
                        onClickStart

                },

            ){
                Icon(
                    imageVector = iconStart,
                    contentDescription = "sc",
                    modifier = Modifier.size(30.dp))
            }
        }

        IconButton (
            onClick = {onClickEnd()}

        ){
            if (iconEnd != null) {
                Icon(
                    imageVector = iconEnd,
                    contentDescription = "sc",
                    modifier = Modifier.size(30.dp)
                )
            }

        }
    }
}