package br.alexandregpereira.amaro.ui.product.list;


import android.os.Bundle;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import br.alexandregpereira.amaro.R;
import br.alexandregpereira.amaro.databinding.ProductsFragmentBinding;
import br.alexandregpereira.amaro.model.product.ProductContract;
import br.alexandregpereira.amaro.ui.product.ProductFragment;

public class ProductsFragmet extends ProductFragment<ProductsFragmentBinding> {

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getViewModel().getProductsLiveData().observe(this, new Observer<List<ProductContract>>() {
            @Override
            public void onChanged(@Nullable List<ProductContract> productContracts) {

            }
        });
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.products_fragment;
    }
}
