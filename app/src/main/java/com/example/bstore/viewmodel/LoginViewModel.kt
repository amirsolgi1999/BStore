package com.example.bstore.viewmodel

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bstore.model.StoreApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val apiService: StoreApiService,
    @ApplicationContext context: Context
):ViewModel(){

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn : StateFlow<Boolean> = _isLoggedIn

    private val _isError = MutableStateFlow(false)
    val isError : StateFlow<Boolean> = _isError

    private val sharedPreferences: SharedPreferences=
        context.getSharedPreferences("app_prefs",Context.MODE_PRIVATE)

    fun login(username:String,password:String){
        viewModelScope.launch {
            Timber.d(" Loading for login")
            try {
                val users = apiService.getUsers().users
                val isValid = users.any { it.username == username && it.password == password }
                _isLoggedIn.value = isValid
                _isError.value=!isValid
                Timber.d("login")
                if (isValid){
                    sharedPreferences.edit().putBoolean("is_logged_in",true).apply()
                }
            }catch (e:Exception){
                _isError.value=true
                Timber.d(" failed to login ${e.message}")

            }
        }
    }

    fun isLoggedIn():Boolean{
        return sharedPreferences.getBoolean("is_logged_in",false)

    }
    fun logout(){
        sharedPreferences.edit().putBoolean("is_logged_in",false).apply()
        _isLoggedIn.value=false
    }
}