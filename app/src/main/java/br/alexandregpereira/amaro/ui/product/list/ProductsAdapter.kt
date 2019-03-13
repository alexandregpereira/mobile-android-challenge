package br.alexandregpereira.amaro.ui.product.list

import br.alexandregpereira.amaro.R
import com.bano.goblin.adapter.BaseAdapter
import br.alexandregpereira.amaro.databinding.ProductItemBinding
import br.alexandregpereira.amaro.model.product.ProductContract
import br.alexandregpereira.amaro.ui.product.ProductUiData

@Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
class ProductsAdapter : BaseAdapter<ProductContract, ProductItemBinding>(
    layoutRes = R.layout.product_item
) {

    override fun onBindViewHolder(binding: ProductItemBinding, product: ProductContract) {
        binding.product = product
    }
}