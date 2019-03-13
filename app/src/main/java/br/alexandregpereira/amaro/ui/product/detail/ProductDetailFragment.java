package br.alexandregpereira.amaro.ui.product.detail;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import br.alexandregpereira.amaro.R;
import br.alexandregpereira.amaro.databinding.ProductDetailFragmentBinding;
import br.alexandregpereira.amaro.model.product.ProductContract;
import br.alexandregpereira.amaro.ui.product.ProductFragment;

public class ProductDetailFragment extends ProductFragment<ProductDetailFragmentBinding> {

    private static final String NAME_KEY = "NAME_KEY";

    private String productName;

    public static ProductDetailFragment newInstance(@NonNull String productName) {
        Bundle bundle = new Bundle();
        bundle.putString(NAME_KEY, productName);
        ProductDetailFragment fragment = new ProductDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        productName = getArguments().getString(NAME_KEY);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ProductContract product = getViewModel().getProductByName(productName);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.product_detail_fragment;
    }


}
