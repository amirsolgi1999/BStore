package com.example.bstore.viewmodel

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bstore.model.appDatabase.StoreDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val db: StoreDatabase,
) : ViewModel() {


    fun clearData() {
        viewModelScope.launch {
            try {
                Timber.d(" Loading for clear Wishlist and cart")
                db.wishlistDao().clear()
                db.cartDao().clear()
                Timber.d(" Clear Wishlist and cart")
            }catch (e:Exception){
                Timber.e(e)
            }
        }
    }
}
