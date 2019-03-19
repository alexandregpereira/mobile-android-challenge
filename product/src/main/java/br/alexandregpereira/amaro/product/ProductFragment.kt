package br.alexandregpereira.amaro.product

import androidx.lifecycle.ViewModelProviders
import br.alexandregpereira.amaro.ui.BaseFragment

abstract class ProductFragment : BaseFragment() {

    protected val viewModel: ProductViewModel by lazy {
        ViewModelProviders.of(activity!!).get(ProductViewModel::class.java)
    }
}