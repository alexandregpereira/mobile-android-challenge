package br.alexandregpereira.amaro.model.product

import com.google.gson.annotations.SerializedName

class Product : ProductContract {

    override var name = ""
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
}