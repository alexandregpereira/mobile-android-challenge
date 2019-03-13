package br.alexandregpereira.amaro.ui.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.alexandregpereira.amaro.model.product.ProductContract

class ProductViewModel : ViewModel() {

    private val liveData = MutableLiveData<List<ProductContract>?>()

    fun getProducts(): LiveData<List<ProductContract>?> {
        return liveData
    }

    fun getProductByName(name: String): ProductContract? = liveData.value?.find { it.name == name }
}