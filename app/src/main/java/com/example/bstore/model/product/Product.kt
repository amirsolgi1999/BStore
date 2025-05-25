package com.example.bstore.model.product

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id:Int,
    val title:String,
    val price:Double,
    val description:String,
    val category:String,
    val image:String
)