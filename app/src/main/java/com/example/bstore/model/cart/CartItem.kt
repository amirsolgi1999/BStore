package com.example.bstore.model.cart

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart")
data class CartItem(
    @PrimaryKey val id: Int,
    val title: String,
    val price: Double,
    val image: String
)
