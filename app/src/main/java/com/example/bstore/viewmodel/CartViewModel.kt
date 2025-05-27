package com.example.bstore.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bstore.model.appDatabase.StoreDatabase
import com.example.bstore.model.cart.CartItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class CartViewModel @Inject constructor(
    private val db: StoreDatabase
) : ViewModel() {

    private val _cart = MutableStateFlow<List<CartItem>>(emptyList())
    val cart: StateFlow<List<CartItem>> = _cart

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _isError = mutableStateOf<String?>(null)
    val isError: State<String?> = _isError

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    init {
        loadCart()
    }


     fun loadCart() {
        viewModelScope.launch {
            _isLoading.value=true
            Timber.d(" wait for load cart")
            try {
               val response= db.cartDao().getAll()
                _cart.value=response
                Timber.d(" Load cart")
            }catch (e:Exception){
                _isError.value="${e.message}"
                Timber.d(e.message," can't load cart product")
                _message.value="can't load load cart product"
            }finally {
                _isLoading.value=false
            }


        }
    }

    fun removeFromCart(id: Int) {
        viewModelScope.launch {
            try {
                db.cartDao().delete(id)
                Timber.d("Removed item")
                loadCart()

            } catch (e: Exception) {
                Timber.e(e.message)
                _message.value="can't remove cart product!"


            }
        }
    }

    fun clearCart(){
        viewModelScope.launch {
            try {
                db.cartDao().clear()
                Timber.d(" clear cart")
                _message.value="Pay!"

            }catch (e:Exception){
                Timber.e(e.message,"cant clear cart!")
                _message.value="can't clear cart!"

            }
        }
    }
}