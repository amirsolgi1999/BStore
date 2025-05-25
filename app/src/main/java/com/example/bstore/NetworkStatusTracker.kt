package com.example.bstore

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL


class NetworkStatusTracker(context: Context) {
    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val _isConnected = MutableLiveData<Boolean>()
    val isConnected: LiveData<Boolean> = _isConnected
    private val _isInternetWorking = MutableLiveData<Boolean>()
    val isInternetWorking: LiveData<Boolean> = _isInternetWorking

    init {
        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                _isConnected.postValue(true)
                checkInternetAvailability()
            }

            override fun onLost(network: Network) {
                _isConnected.postValue(false)
                _isInternetWorking.postValue(false)
            }
        }

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(request, networkCallback)
        checkInternetAvailability() // چک اولیه هنگام شروع
    }

    fun isNetworkAvailable(): Boolean {
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    private fun checkInternetAvailability() {
        kotlinx.coroutines.GlobalScope.launch(Dispatchers.IO) {
            val isWorking = try {
                val url = URL("https://www.google.com") // یا آدرس API خودتون
                val connection = url.openConnection() as HttpURLConnection
                connection.connectTimeout = 2000 // 2 ثانیه
                connection.connect()
                connection.responseCode == 200
            } catch (e: IOException) {
                false
            }
            _isInternetWorking.postValue(isWorking)
        }
    }
}