package com.example.bstore.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bstore.model.appDatabase.StoreDatabase
import com.example.bstore.model.cart.CartItem
import com.example.bstore.model.product.Product
import com.example.bstore.model.wishlist.WishlistItem
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class WishlistViewModel @Inject constructor(
    private val db:StoreDatabase,
    @ApplicationContext val  context: Context
):ViewModel(){

    private val _wishlist = MutableStateFlow<List<WishlistItem>>(emptyList())
    val wishlist : StateFlow<List<WishlistItem>> = _wishlist

    private val _message = MutableStateFlow<String?>(null)
    val message:StateFlow<String?> = _message

    init {
        loadWishlist()

    }

     fun loadWishlist(){
        viewModelScope.launch {
            _wishlist.value=db.wishlistDao().getAll()
            Timber.d(" Load Wishlist")

        }
    }
    fun addToCart(item: WishlistItem){
        viewModelScope.launch {
            Timber.d(" Loading add to cart")
                try {
                val isAlreadyInCart = db.cartDao().getAllIds().contains(item.id)
                if (!isAlreadyInCart){
                    db.cartDao().insert(
                        CartItem(item.id,item.title,item.price,item.image)
                    )
                    Timber.d(" add to cart")
                    _message.value="Add to cart"
                }else{
                    _message.value="Already in cart"
                    Timber.d(" in cart")


                }
            }catch (e:Exception){
                    _message.value="failed to add"
                    Timber.d("failed to add: ${e.message}")


                }
        }
    }

    fun removeFromWishlist(id:Int){
        viewModelScope.launch {
            db.wishlistDao().delete(id)
            _message.value="remove from wishlist"
            Timber.d(" Remove from Wishlist")
            loadWishlist()
        }
    }
}