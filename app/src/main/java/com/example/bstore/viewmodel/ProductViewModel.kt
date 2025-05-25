package com.example.bstore.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bstore.NetworkStatusTracker
import com.example.bstore.model.appDatabase.StoreDatabase
import com.example.bstore.model.cart.CartItem
import com.example.bstore.model.product.Product
import com.example.bstore.model.StoreApiService
import com.example.bstore.model.wishlist.WishlistItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    val productApiService: StoreApiService,
    val db:StoreDatabase,
  val  networkStatusTracker: NetworkStatusTracker
):ViewModel(){

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products :StateFlow<List<Product>> = _products

    private val _newInProducts = MutableStateFlow<List<Product>>(emptyList())
    val newInProducts :StateFlow<List<Product>> = _newInProducts

    private val _isLoading = mutableStateOf(false)
    val isLoading : State<Boolean> = _isLoading

    private val _isError = mutableStateOf<String?>(null)
    val isError : State<String?> = _isError

    private val _selectedCategories = mutableStateOf<Set<String>>(emptySet())
    val selectedCategories :State<Set<String>> =_selectedCategories

    private val _searchProduct = mutableStateOf("")
    val searchProduct : State<String> = _searchProduct

    private val _filteredProducts = mutableStateOf<List<Product>>(emptyList())
    val filteredProducts : State<List<Product>> =_filteredProducts

    private val _filteredNewProducts = mutableStateOf<List<Product>>(emptyList())
    val filteredNewProducts : State<List<Product>> =_filteredNewProducts

    private val _catProduct = MutableStateFlow<List<Product>>(emptyList())
    val catProduct : StateFlow<List<Product>> = _catProduct

    private val _wishlistIds = MutableStateFlow<Set<Int>>(emptySet())
    val wishlistIds:StateFlow<Set<Int>> = _wishlistIds

    private val _cartIds=MutableStateFlow<Set<Int>>(emptySet())
    val cartIds:StateFlow<Set<Int>> = _cartIds

    private val _message = MutableStateFlow<String?>(null)
    val message:StateFlow<String?> = _message

    init {
        getPopularProducts()
        getNewINProducts(2)
        loadWishlistAndCartIds()
    }

    fun getPopularProducts(){
        viewModelScope.launch {
            _isLoading.value=true
            _products.value=try {
                productApiService.getPopularProducts().products
            }catch (e:Exception){
                _isError.value="${e.message}"
                emptyList()
            }finally {
                _isLoading.value=false
            }
        }
    }

    fun getNewINProducts(page:Int){
        viewModelScope.launch {
            _isLoading.value=true
            _newInProducts.value=try {
                productApiService.newInProducts(2).products
            }catch (e:Exception){
                _isError.value="${e.message}"
                emptyList()
            }finally {
                _isLoading.value=false
            }
        }
    }

    fun loadProductsByCategories(categories:Set<String>){
        viewModelScope.launch {
            _isLoading.value=true
            try {
                if (categories.isEmpty()){
                    getPopularProducts()
                    _isError.value=null
                }else{
                    val allProducts = mutableListOf<Product>()
                    categories.forEach { category ->
                        val response = productApiService.getWithCategory(category)
                        allProducts.addAll(response.products)
                    }
                    _products.value=allProducts.distinctBy { it.id }
                    _selectedCategories.value=categories
                }
            }catch (e:Exception){
                _isError.value="${e.message}"
            }finally {
                _isLoading.value=false
            }
        }
    }

    fun updatedCategories(categories: Set<String>){
        _selectedCategories.value=categories
        loadProductsByCategories(categories)
    }

    fun getProductById(id:Int):Product?{
        val product = products.value.find { it.id==id }
        return product
    }

    fun filteredProducts(){
        val query = _searchProduct.value
        _filteredProducts.value=if (query.isEmpty()){
            products.value
        }else{
            _products.value.filter { it.title.contains(query, ignoreCase = true) }
        }
        _filteredNewProducts.value=if (query.isEmpty()){
            newInProducts.value
        }else{
            _products.value.filter { it.title.contains(query, ignoreCase = true) }
        }
    }

    fun onSearchQueryChange(query:String){
        _searchProduct.value=query
        filteredProducts()

    }

    fun getProductByCategory(category:String){
        if (networkStatusTracker.isNetworkAvailable()){
        viewModelScope.launch (Dispatchers.IO){
            val response = productApiService.getWithCategory(category)
            _products.value=response.products.filter { it.category.equals(category, ignoreCase = true) }
        }
        }else{
            println("Error")
        }
    }

    fun loadWishlistAndCartIds(){
        viewModelScope.launch {
            try {
                _wishlistIds.value=db.wishlistDao().getAllIds().toSet()
                _cartIds.value=db.cartDao().getAllIds().toSet()
            }catch (e:Exception){
                _isError.value=e.message ?: "Failed to load wishlist/cart"
            }
        }
    }

    fun addToWishlist(product: Product){
        viewModelScope.launch {
            try {
                val isAlreadyInWishlist= _wishlistIds.value.contains(product.id)
                if (!isAlreadyInWishlist){
                    db.wishlistDao().insert(
                        WishlistItem(product.id,product.title,product.price,product.image)
                    )
                    loadWishlistAndCartIds()
                    _message.value="${product.title} added to wishlist"
                }else{
                    _message.value="${product.title} is already added"
                }
            }catch (e:Exception){
                _message.value="Failed to add ${product.title} to wishlist : ${e.message}"
            }
        }
    }

    fun addToCart(product: Product){
        viewModelScope.launch {
            try {
                val isAlreadyInCart = _cartIds.value.contains(product.id)
                if (!isAlreadyInCart){
                    db.cartDao().insert(
                        CartItem(product.id,product.title,product.price,product.image)
                    )
                    loadWishlistAndCartIds()
                    _message.value="${product.title} added to cart"

                }else{
                    _message.value="${product.title} is already added"

                }
            }catch (e:Exception){
                _message.value="Failed to add ${product.title} to cart : ${e.message}"
            }
        }
    }

    fun clearMessage(){
        _message.value=null
    }

    fun getProductByIdWC(productId:Int):StateFlow<Product?>{
        return _products.map { products ->
            products.find { it.id==productId }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000),null)
    }


}