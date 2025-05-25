package com.example.bstore.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bstore.model.StoreApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val apiService: StoreApiService
):ViewModel(){

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn : StateFlow<Boolean> = _isLoggedIn

    private val _isError = MutableStateFlow(false)
    val isError : StateFlow<Boolean> = _isError

    fun login(username:String,password:String){
        viewModelScope.launch {
            try {
                val users = apiService.getUsers().users
                val isValid = users.any { it.username == username && it.password == password }
                _isLoggedIn.value = isValid
                _isError.value=!isValid
            }catch (e:Exception){
                _isError.value=true
            }
        }
    }
}