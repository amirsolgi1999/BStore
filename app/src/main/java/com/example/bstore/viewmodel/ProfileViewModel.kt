package com.example.bstore.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bstore.model.appDatabase.StoreDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val db: StoreDatabase
) : ViewModel() {
    fun clearData() {
        viewModelScope.launch {
            db.wishlistDao().clear()
            db.cartDao().clear()
        }
    }
}
