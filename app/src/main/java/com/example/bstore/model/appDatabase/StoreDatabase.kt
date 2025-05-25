package com.example.bstore.model.appDatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.bstore.model.cart.CartDao
import com.example.bstore.model.cart.CartItem
import com.example.bstore.model.wishlist.WishlistDao
import com.example.bstore.model.wishlist.WishlistItem

@Database(
    entities = [WishlistItem::class,CartItem::class],
    version = 1,
    exportSchema = false
)
abstract class StoreDatabase : RoomDatabase() {

    abstract fun wishlistDao():WishlistDao
    abstract fun cartDao():CartDao
}