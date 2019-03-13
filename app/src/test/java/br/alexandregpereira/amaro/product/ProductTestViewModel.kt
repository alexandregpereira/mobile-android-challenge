package br.alexandregpereira.amaro.product

import br.alexandregpereira.amaro.ui.product.ProductViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ProductTestViewModel : ProductViewModel() {

    override fun getCoroutineScopeMain(block: suspend CoroutineScope.() -> Unit): Job =
        runBlocking {
            this.launch(block = block)
        }
}