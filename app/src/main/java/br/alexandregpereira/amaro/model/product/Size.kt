package br.alexandregpereira.amaro.model.product

data class Size (
    var available: Boolean = false,
    var size: String? = null,
    var sku: String? = null
)