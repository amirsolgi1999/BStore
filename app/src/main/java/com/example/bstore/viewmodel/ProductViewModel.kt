package com.example.bstore.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bstore.model.appDatabase.StoreDatabase
import com.example.bstore.model.cart.CartItem
import com.example.bstore.model.product.Product
import com.example.bstore.model.StoreApiService
import com.example.bstore.model.wishlist.WishlistItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject



@HiltViewModel
class ProductViewModel @Inject constructor(
    val productApiService: StoreApiService,
    val db: StoreDatabase,
) : ViewModel() {

    private val _popularProducts = MutableStateFlow<List<Product>>(emptyList())
    val popularProducts: StateFlow<List<Product>> = _popularProducts

    private val _newInProducts = MutableStateFlow<List<Product>>(emptyList())
    val newInProducts: StateFlow<List<Product>> = _newInProducts

    private val _otherProduct = MutableStateFlow<List<Product>>(emptyList())
    val otherProduct :StateFlow<List<Product>> = _otherProduct

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _isError = mutableStateOf<String?>(null)
    val isError: State<String?> = _isError

    private val _selectedCategories = mutableStateOf<Set<String>>(emptySet())
    val selectedCategories: State<Set<String>> = _selectedCategories

    private val _searchProduct = mutableStateOf("")
    val searchProduct: State<String> = _searchProduct

    private val _filteredPopularProducts = mutableStateOf<List<Product>>(emptyList())
    val filteredPopularProducts: State<List<Product>> = _filteredPopularProducts

    private val _filteredNewProducts = mutableStateOf<List<Product>>(emptyList())
    val filteredNewProducts: State<List<Product>> = _filteredNewProducts

    private val _catProduct = MutableStateFlow<List<Product>>(emptyList())
    val catProduct: StateFlow<List<Product>> = _catProduct

    private val _wishlistIds = MutableStateFlow<Set<Int>>(emptySet())
    val wishlistIds: StateFlow<Set<Int>> = _wishlistIds

    private val _cartIds = MutableStateFlow<Set<Int>>(emptySet())
    val cartIds: StateFlow<Set<Int>> = _cartIds

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    init {
        loadPopularProducts()
        loadNewInProducts()
        loadWishlistAndCartIds()
        loadOtherProducts()
    }

    fun loadPopularProducts() {
        viewModelScope.launch {
            Timber.d("Loading popular products...")
            _isLoading.value = true
            try {
                val products = productApiService.getPopularProducts().products
                _popularProducts.value = products
                Timber.d("Loaded  popular products", products.size)
            } catch (e: Exception) {
                Timber.e(e, "Failed to load popular products")
                _isError.value = "${e.message}"
                _popularProducts.value = emptyList()
            } finally {
                _isLoading.value = false
                Timber.d("Finished loading popular products")
            }
        }
    }

    fun loadNewInProducts() {
        viewModelScope.launch {
            Timber.d("Loading new in products...")
            _isLoading.value = true
            try {
                val products = productApiService.getNewInProducts(2).products
                _newInProducts.value = products
                Timber.d("Loaded  new in products", products.size)
            } catch (e: Exception) {
                Timber.e(e, "Failed to load new in products")
                _isError.value = "${e.message}"
                _newInProducts.value = emptyList()
            } finally {
                _isLoading.value = false
                Timber.d("Finished loading new in products")
            }
        }
    }
    fun loadOtherProducts() {
        viewModelScope.launch {
            Timber.d("Loading new in products...")
            _isLoading.value = true
            try {
                val products = productApiService.getNewInProducts(3).products
                _otherProduct.value = products
                Timber.d("Loaded  new in products", products.size)
            } catch (e: Exception) {
                Timber.e(e, "Failed to load new in products")
                _isError.value = "${e.message}"
                _otherProduct.value = emptyList()
            } finally {
                _isLoading.value = false
                Timber.d("Finished loading new in products")
            }
        }
    }

    fun loadProductsByCategories(categories: Set<String>) {
        viewModelScope.launch {
            Timber.d("Loading products for categories", categories)
            _isLoading.value = true
            try {
                if (categories.isEmpty()) {
                    Timber.d("No categories selected, loading popular products")
                    loadPopularProducts()
                    _isError.value = null
                } else {
                    val allProducts = mutableListOf<Product>()
                    categories.forEach { category ->
                        Timber.d("geting products for category: ", category)
                        val response = productApiService.getWithCategory(category)
                        allProducts.addAll(response.products)
                    }
                    _popularProducts.value = allProducts.distinctBy { it.id }
                    _selectedCategories.value = categories
                    Timber.d("Loaded  products for categories", allProducts.size)
                }
            } catch (e: Exception) {
                Timber.e(e, "Failed to load products by categories")
                _isError.value = "${e.message}"
            } finally {
                _isLoading.value = false
                Timber.d("Finished loading products by categories")
            }
        }
    }

    fun updatedCategories(categories: Set<String>) {
        Timber.d("Updating selected categories to", categories)
        _selectedCategories.value = categories
        loadProductsByCategories(categories)
    }

    fun searchProducts() {
        val query = _searchProduct.value
        Timber.d("Searching products with query", query)
        _filteredPopularProducts.value = if (query.isEmpty()) {
            popularProducts.value
        } else {
            _popularProducts.value.filter { it.title.contains(query, ignoreCase = true) }
        }
        _filteredNewProducts.value = if (query.isEmpty()) {
            newInProducts.value
        } else {
            _newInProducts.value.filter { it.title.contains(query, ignoreCase = true) }
        }
        Timber.d(" filtered popular products and filtered new products",
            _filteredPopularProducts.value.size, _filteredNewProducts.value.size)

    }

    fun onSearchQueryChange(query: String) {
        Timber.d("Search query changed to", query)
        _searchProduct.value = query
        searchProducts()
    }

    fun getProductByCategory(category: String) {
        viewModelScope.launch {
            Timber.d("Loading products for category", category)
            try {
                val products = productApiService.getWithCategory(category).products
                _catProduct.value = products
                _message.value="Get Product"
                Timber.d("Loaded %d products for category ", products.size, category)
            } catch (e: Exception) {
                Timber.e(e, "Failed to load products for category", category)
                _isError.value = "${e.message}"
                _message.value=e.message
                _catProduct.value = emptyList()
            } finally {
                Timber.d("Finished loading products for category", category)
            }
        }
    }



    fun loadWishlistAndCartIds() {
        viewModelScope.launch {
            Timber.d("Loading wishlist and cart IDs...")
            try {
                _wishlistIds.value = db.wishlistDao().getAllIds().toSet()
                _cartIds.value = db.cartDao().getAllIds().toSet()
                Timber.d("Loaded  wishlist IDs and  cart IDs", _wishlistIds.value.size, _cartIds.value.size)
            } catch (e: Exception) {
                Timber.e(e, "Failed to load wishlist or cart IDs")
                _isError.value = e.message ?: "Failed to load wishlist/cart"
            }
        }
    }

    fun addToWishlist(product: Product) {
        viewModelScope.launch {
            Timber.d("Adding product  to wishlist", product.title)
            try {
                val isAlreadyInWishlist = _wishlistIds.value.contains(product.id)
                if (!isAlreadyInWishlist) {
                    db.wishlistDao().insert(
                        WishlistItem(product.id, product.title, product.price, product.image)
                    )
                    loadWishlistAndCartIds()
                    _message.value = "added to wishlist"
                    Timber.d("Product  added to wishlist", product.title)
                } else {
                    _message.value = "is already added"
                    Timber.w("Product  already in wishlist", product.title)
                }
            } catch (e: Exception) {
                Timber.e(e, "Failed to add product to wishlist", product.title)
                _message.value = "Failed to add  to wishlist"
            }
        }
    }

    fun addToCart(product: Product) {
        viewModelScope.launch {
            Timber.d("Adding product  to cart", product.title)
            try {
                val isAlreadyInCart = _cartIds.value.contains(product.id)
                if (!isAlreadyInCart) {
                    db.cartDao().insert(
                        CartItem(product.id, product.title, product.price, product.image)
                    )
                    loadWishlistAndCartIds()
                    _message.value = "added to cart"
                    Timber.d("Product  added to cart", product.title)
                } else {
                    _message.value = "is already added"
                    Timber.w("Product  already in cart", product.title)
                }
            } catch (e: Exception) {
                Timber.e(e, "Failed to add product to cart", product.title)
                _message.value = "Failed to add to cart"
            }
        }
    }

    fun clearMessage() {
        Timber.d("Clearing message")
        _message.value = null
    }

    fun getCarProByWCId(productId: Int):StateFlow<Product?>{
        Timber.d("Getting other product by ID", productId)
        return _otherProduct.map { product ->
            product.find { it.id == productId }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000),null)
    }

    fun getPopularProductByIdWC(productId: Int): StateFlow<Product?> {
        Timber.d("Getting popular product by ID", productId)
        return _popularProducts.map { products ->
            products.find { it.id == productId }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
    }

    fun getNewInProductByIdWC(productId: Int): StateFlow<Product?> {
        Timber.d("Getting new in product by ID", productId)
        return _newInProducts.map { newIn ->
            newIn.find { it.id == productId }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
    }
}