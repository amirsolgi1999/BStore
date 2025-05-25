package com.example.bstore.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bstore.model.appDatabase.StoreDatabase
import com.example.bstore.model.cart.CartItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CartViewModel @Inject constructor(
    private val db: StoreDatabase
) : ViewModel() {

    private val _cart = MutableStateFlow<List<CartItem>>(emptyList())
    val cart: StateFlow<List<CartItem>> = _cart

    init {
        loadCart()
    }


     fun loadCart() {
        viewModelScope.launch {
            _cart.value = db.cartDao().getAll()
        }
    }

    fun removeFromCart(id: Int) {
        viewModelScope.launch {
            db.cartDao().delete(id)
            loadCart()
        }
    }

    fun clearCart(){
        viewModelScope.launch {
            db.cartDao().clear()
        }
    }
}