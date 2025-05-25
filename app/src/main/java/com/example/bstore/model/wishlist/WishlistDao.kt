package com.example.bstore.model.wishlist

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WishlistDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: WishlistItem)

    @androidx.room.Query("DELETE FROM wishlist WHERE id = :id")
    suspend fun delete(id: Int)

    @androidx.room.Query("SELECT * FROM wishlist")
    suspend fun getAll(): List<WishlistItem>

    @androidx.room.Query("DELETE FROM wishlist")
    suspend fun clear()

    @androidx.room.Query("SELECT id FROM wishlist")
    suspend fun getAllIds(): List<Int>
}