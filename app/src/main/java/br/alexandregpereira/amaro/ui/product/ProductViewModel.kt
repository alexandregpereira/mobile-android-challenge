package br.alexandregpereira.amaro.ui.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.alexandregpereira.amaro.model.product.ProductContract
import br.alexandregpereira.amaro.exception.ConnectionException
import br.alexandregpereira.amaro.exception.ConnectionError
import br.alexandregpereira.amaro.remote.product.ProductRemote
import br.alexandregpereira.amaro.ui.product.list.ProductsOrder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

open class ProductViewModel : ViewModel() {

    private val liveData = MutableLiveData<List<ProductContract>?>()
    protected open val remote = ProductRemote()
    private val loadingLiveData = MutableLiveData<Boolean?>()
    private val errorLiveData = MutableLiveData<ConnectionError?>()

    private var order = ProductsOrder.ANY
    private var anyOrderProducts: List<ProductContract>? = null

    fun getProductsLiveData(): LiveData<List<ProductContract>?> {
        loadProductsRemote()
        return liveData
    }

    fun getProducts(): List<ProductContract>? = liveData.value

    fun refresh() {
        getProductsLiveData()
    }

    fun getProductByName(name: String): ProductContract? = liveData.value?.find { it.name == name }

    private fun loadProductsRemote() = getCoroutineScopeMain {
        if (loadingLiveData.value == true) return@getCoroutineScopeMain
        loadingLiveData.value = true
        try {
            anyOrderProducts = remote.getProducts()
            liveData.value = order(anyOrderProducts)
        } catch (ex: ConnectionException) {
            errorLiveData.value = ex.error
        }
        loadingLiveData.value = false
    }

    fun getLoadingLiveData(): LiveData<Boolean?> = loadingLiveData

    fun getErrorLiveData(): LiveData<ConnectionError?> = errorLiveData

    fun isLiveDataEmpty(): Boolean = liveData.value.isNullOrEmpty()

    fun orderBy(order: ProductsOrder) {
        val anyOrderProducts = anyOrderProducts
        if (order == this.order || anyOrderProducts.isNullOrEmpty()) return
        this.order = order

        liveData.value = order(anyOrderProducts)
    }

    private fun order(products: List<ProductContract>?): List<ProductContract>? {
        if (order == ProductsOrder.ANY) {
            return products
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