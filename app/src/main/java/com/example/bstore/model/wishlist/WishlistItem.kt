package com.example.bstore.model.wishlist

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wishlist")
data class WishlistItem(
    @PrimaryKey
    val id:Int,
    val title:String,
    val price:Double,
    val image:String
)