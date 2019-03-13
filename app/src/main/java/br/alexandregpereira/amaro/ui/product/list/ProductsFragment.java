package br.alexandregpereira.amaro.ui.product.list;


import android.os.Bundle;
import android.view.View;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import br.alexandregpereira.amaro.R;
import br.alexandregpereira.amaro.databinding.ProductsFragmentBinding;
import br.alexandregpereira.amaro.model.product.ProductContract;
import br.alexandregpereira.amaro.ui.product.ProductFragment;

public class ProductsFragment extends ProductFragment<ProductsFragmentBinding> {

    private final ProductsAdapter adapter = new ProductsAdapter();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getBinding().productRecyclerView.setHasFixedSize(true);
        getBinding().productRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        getBinding().productRecyclerView.setAdapter(adapter);

        getViewModel().getProductsLiveData().observe(this, new Observer<List<ProductContract>>() {
            @Override
            public void onChanged(@Nullable List<ProductContract> productContracts) {
                if (productContracts == null) return;
                adapter.setItems(productContracts);
            }
        });
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.products_fragment;
    }
}
