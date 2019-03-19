package br.alexandregpereira.amaro.remote.product

import br.alexandregpereira.amaro.model.product.ProductContract
import br.alexandregpereira.amaro.exception.ConnectionException
import br.alexandregpereira.amaro.exception.ConnectionError
import br.alexandregpereira.amaro.remote.Remote
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

open class  ProductRemote {

    @Throws(ConnectionException::class)
    open suspend fun getProducts(): List<ProductContract>? {
        return try {
            Remote.getApi(ProductApi::class.java).getProductsAsync().await().products
        } catch (e: Exception) {
            val error = when(e) {
                is ConnectException -> ConnectionError.NO_CONNECTION
                is SocketTimeoutException -> ConnectionError.TIMEOUT
                is UnknownHostException -> ConnectionError.NO_INTERNET
                else -> ConnectionError.UNKNOWN
            }

            throw ConnectionException(error)
        }
    }
}