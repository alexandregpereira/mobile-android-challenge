package br.alexandregpereira.amaro.model.product

interface ProductContract {

    val name: String
    val onSale: Boolean
    val regularPrice: String
    val actualPrice: String
    val discountPercentage: String
    val installments: String
    val image: String
    val sizes: List<Size>

    fun hasDiscount() = discountPercentage.isNotEmpty() && actualPrice != regularPrice

    fun getActualPriceNumber(): Double {
        return actualPrice
            .replace("R$", "")
            .replace(",", ".")
            .trim()
            .toDouble()
    }
}