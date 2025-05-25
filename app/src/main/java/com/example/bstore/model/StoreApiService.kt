package com.example.bstore.model

import com.example.bstore.model.product.ProductResponse
import com.example.bstore.model.user.UserResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface StoreApiService {

    @GET("products")
    suspend fun getPopularProducts(): ProductResponse

    @GET("products")
    suspend fun newInProducts(
        @Query("page") page:Int=2
    ): ProductResponse

    @GET("products/category")
    suspend fun getWithCategory(
        @Query("type") category:String
    ): ProductResponse

    @GET("api/users")
    suspend fun getUsers(): UserResponse

}