package br.alexandregpereira.amaro.model.product

import com.google.gson.annotations.SerializedName

class Product : ProductContract {

    override var name = ""
    @SerializedName("code_color")
    override val codeColor: String = ""
    @SerializedName("on_sale")
    override var onSale: Boolean = false
    @SerializedName("regular_price")
    override var regularPrice: String = ""
    @SerializedName("actual_price")
    override var actualPrice: String = ""
    @SerializedName("discount_percentage")
    override var discountPercentage: String = ""
    override var installments: String = ""
    override var image: String = ""
    override var sizes: List<Size> = mutableListOf()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Product

        if (codeColor != other.codeColor) return false

        return true
    }

    override fun hashCode(): Int {
        return codeColor.hashCode()
    }

    override fun toString(): String {
        return "Product(name='$name', codeColor='$codeColor', onSale=$onSale, regularPrice='$regularPrice', actualPrice='$actualPrice', discountPercentage='$discountPercentage', installments='$installments', image='$image')"
    }
}