package br.alexandregpereira.amaro.ui.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.alexandregpereira.amaro.model.product.ProductContract
import br.alexandregpereira.amaro.exception.ConnectionException
import br.alexandregpereira.amaro.exception.ConnectionError
import br.alexandregpereira.amaro.remote.product.ProductRemote
import br.alexandregpereira.amaro.ui.product.list.ProductsFilter
import br.alexandregpereira.amaro.ui.product.list.ProductsOrder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

open class ProductViewModel : ViewModel() {

    protected open val remote = ProductRemote()

    private val liveData = MutableLiveData<List<ProductContract>?>()
    private val loadingLiveData = MutableLiveData<Boolean?>()
    private val errorLiveData = MutableLiveData<ConnectionError?>()
    private val filterSet = mutableSetOf<ProductsFilter>()

    private var order = ProductsOrder.ANY
    private var anyOrderProducts: List<ProductContract>? = null
    private var cached = false

    fun getProductsLiveData(): LiveData<List<ProductContract>?> {
        if (!cached) loadProductsRemote()
        return liveData
    }

    fun getProducts(): List<ProductContract>? = liveData.value

    fun refresh() {
        cached = false
        getProductsLiveData()
    }

    fun getProductByCodeColor(codeColor: String): ProductContract? = liveData.value?.find { it.codeColor == codeColor }

    fun getLoadingLiveData(): LiveData<Boolean?> = loadingLiveData

    fun getErrorLiveData(): LiveData<ConnectionError?> = errorLiveData

    fun isLiveDataEmpty(): Boolean = liveData.value.isNullOrEmpty()

    fun orderBy(order: ProductsOrder) {
        if (order == this.order) return
        this.order = order

        val anyOrderProducts = anyOrderProducts
        if (anyOrderProducts.isNullOrEmpty()) return
        liveData.value = filterAndOrder(anyOrderProducts)
    }

    fun removeFilter(filter: ProductsFilter): Boolean {
        if (!filterSet.remove(filter)) return false
        liveData.value = filterAndOrder(anyOrderProducts)
        return true
    }

    fun filterBy(filter: ProductsFilter): Boolean {
        if (!filterSet.add(filter)) return false
        liveData.value = filterAndOrder(anyOrderProducts)
        return true
    }

    private fun filter(products: List<ProductContract>?): List<ProductContract>? {
        if (filterSet.isEmpty() || products.isNullOrEmpty()) return products

        return products.filter {
            if (filterSet.contains(ProductsFilter.ON_SALE) && filterSet.contains(ProductsFilter.DISCOUNT))
                it.onSale && it.hasDiscount()
            else if (filterSet.contains(ProductsFilter.ON_SALE))
                it.onSale
            else if (filterSet.contains(ProductsFilter.DISCOUNT))
            it.hasDiscount()
            else
                false
        }
    }

    private fun loadProductsRemote() = getCoroutineScopeMain {
        if (loadingLiveData.value == true) return@getCoroutineScopeMain
        loadingLiveData.value = true
        try {
            anyOrderProducts = remote.getProducts()
            cached = true
            liveData.value = filterAndOrder(anyOrderProducts)
        } catch (ex: ConnectionException) {
            errorLiveData.value = ex.error
        }
        loadingLiveData.value = false
    }

    private fun filterAndOrder(products: List<ProductContract>?): List<ProductContract>? {
        return filter(order(products))
    }

    private fun order(products: List<ProductContract>?): List<ProductContract>? {
        if (order == ProductsOrder.ANY) {
            return anyOrderProducts
        }

        return products?.sortedWith(Comparator { o1, o2 ->
            if (order == ProductsOrder.LOW_HIGH)
                o1.getActualPriceNumber().compareTo(o2.getActualPriceNumber())
            else
                o2.getActualPriceNumber().compareTo(o1.getActualPriceNumber())
        })
    }

    protected open fun getCoroutineScopeMain(block: suspend CoroutineScope.() -> Unit): Job =
        CoroutineScope(Dispatchers.Main).launch(block = block)
}