package br.alexandregpereira.amaro.ui.product.detail

import br.alexandregpereira.amaro.R
import br.alexandregpereira.amaro.model.product.Size
import com.bano.goblin.adapter.BaseAdapter
import br.alexandregpereira.amaro.databinding.SizeItemBinding

@Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
class SizesAdapter : BaseAdapter<Size, SizeItemBinding>(
    layoutRes = R.layout.size_item
) {
    override fun onBindViewHolder(binding: SizeItemBinding, size: Size) {
        binding.size = size
    }
}