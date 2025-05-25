package com.example.bstore.model.product

data class ProductResponse(
    val status:String,
    val message:String,
    val products:List<Product>
)