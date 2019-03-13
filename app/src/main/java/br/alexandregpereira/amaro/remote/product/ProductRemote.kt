package br.alexandregpereira.amaro.remote.product

import br.alexandregpereira.amaro.model.product.ProductContract
import br.alexandregpereira.amaro.remote.Remote

open class ProductRemote {

    open suspend fun getProducts(): List<ProductContract>? {
        return Remote.getApi(ProductApi::class.java).getProductsAsync().await().products
    }
}