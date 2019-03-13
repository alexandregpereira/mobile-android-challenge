package br.alexandregpereira.amaro.ui.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.alexandregpereira.amaro.model.product.ProductContract
import br.alexandregpereira.amaro.remote.product.ProductRemote
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

open class ProductViewModel : ViewModel() {

    private val liveData = MutableLiveData<List<ProductContract>?>()
    protected open val remote = ProductRemote()
    private var loadingLiveData = MutableLiveData<Boolean?>()

    fun getProductsLiveData(): LiveData<List<ProductContract>?> {
        loadProductsRemote()
        return liveData
    }

    fun getProductByName(name: String): ProductContract? = liveData.value?.find { it.name == name }

    private fun loadProductsRemote() = getCoroutineScopeMain {
        if (loadingLiveData.value == true) return@getCoroutineScopeMain
        loadingLiveData.value = true
        liveData.value = remote.getProducts()
        loadingLiveData.value = false
    }

    fun getLoadingLiveData(): LiveData<Boolean?> = loadingLiveData

    protected open fun getCoroutineScopeMain(block: suspend CoroutineScope.() -> Unit): Job =
        CoroutineScope(Dispatchers.Main).launch(block = block)
}