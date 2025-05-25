package com.example.bstore.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bstore.model.appDatabase.StoreDatabase
import com.example.bstore.model.cart.CartItem
import com.example.bstore.model.product.Product
import com.example.bstore.model.wishlist.WishlistItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WishlistViewModel @Inject constructor(
    private val db:StoreDatabase
):ViewModel(){

    private val _wishlist = MutableStateFlow<List<WishlistItem>>(emptyList())
    val wishlist : StateFlow<List<WishlistItem>> = _wishlist

    init {
        loadWishlist()

    }

     fun loadWishlist(){
        viewModelScope.launch {
            _wishlist.value=db.wishlistDao().getAll()
        }
    }
    fun addToCart(item: WishlistItem){
        viewModelScope.launch {
            try {
                val isAlreadyInCart = db.cartDao().getAllIds().contains(item.id)
                if (!isAlreadyInCart){
                    db.cartDao().insert(
                        CartItem(item.id,item.title,item.price,item.image)
                    )
                }else{

                }
            }catch (e:Exception){

            }
        }
    }

    fun removeFromWishlist(id:Int){
        viewModelScope.launch {
            db.wishlistDao().delete(id)
            loadWishlist()
        }
    }
}