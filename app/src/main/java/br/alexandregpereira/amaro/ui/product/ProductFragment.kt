package br.alexandregpereira.amaro.ui.product

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import br.alexandregpereira.amaro.ui.BaseFragment

abstract class ProductFragment<V : ViewDataBinding> : BaseFragment<V>() {

    protected val viewModel: ProductViewModel by lazy {
        ViewModelProviders.of(activity!!).get(ProductViewModel::class.java)
    }
}