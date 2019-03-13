package br.alexandregpereira.amaro.remote.product

import kotlinx.coroutines.Deferred
import retrofit2.http.GET

interface ProductApi {

    @GET("v2/59b6a65a0f0000e90471257d")
    fun getProductsAsync(): Deferred<ProductApiData>
}