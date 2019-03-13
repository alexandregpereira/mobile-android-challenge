package br.alexandregpereira.amaro.ui.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.alexandregpereira.amaro.model.product.ProductContract
import br.alexandregpereira.amaro.exception.ConnectionException
import br.alexandregpereira.amaro.exception.ConnectionError
import br.alexandregpereira.amaro.remote.product.ProductRemote
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

open class ProductViewModel : ViewModel() {

    private val liveData = MutableLiveData<List<ProductContract>?>()
    protected open val remote = ProductRemote()
    private val loadingLiveData = MutableLiveData<Boolean?>()
    private val errorLiveData = MutableLiveData<ConnectionError?>()

    fun getProductsLiveData(): LiveData<List<ProductContract>?> {
        loadProductsRemote()
        return liveData
    }

    fun refresh() {
        getProductsLiveData()
    }

    fun getProductByName(name: String): ProductContract? = liveData.value?.find { it.name == name }

    private fun loadProductsRemote() = getCoroutineScopeMain {
        if (loadingLiveData.value == true) return@getCoroutineScopeMain
        loadingLiveData.value = true
        try {
            liveData.value = remote.getProducts()
        } catch (ex: ConnectionException) {
            errorLiveData.value = ex.error
        }
        loadingLiveData.value = false
    }

    fun getLoadingLiveData(): LiveData<Boolean?> = loadingLiveData
    fun getErrorLiveData(): LiveData<ConnectionError?> = errorLiveData
    fun isLiveDataEmpty(): Boolean = liveData.value.isNullOrEmpty()

    protected open fun getCoroutineScopeMain(block: suspend CoroutineScope.() -> Unit): Job =
        CoroutineScope(Dispatchers.Main).launch(block = block)
}