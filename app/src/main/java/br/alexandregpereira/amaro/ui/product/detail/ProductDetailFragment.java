package br.alexandregpereira.amaro.ui.product.detail;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import br.alexandregpereira.amaro.R;
import br.alexandregpereira.amaro.databinding.ProductDetailFragmentBinding;
import br.alexandregpereira.amaro.model.product.ProductContract;
import br.alexandregpereira.amaro.ui.product.ProductFragment;

public class ProductDetailFragment extends ProductFragment<ProductDetailFragmentBinding> {

    private static final String NAME_KEY = "NAME_KEY";

    private String productName;
    private final SizesAdapter sizesAdapter = new SizesAdapter();

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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getBinding().sizesRecycler.setHasFixedSize(true);
        getBinding().sizesRecycler.setAdapter(sizesAdapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getBinding().closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentActivity activity = getActivity();
                if (activity != null) activity.onBackPressed();
            }
        });

        ProductContract product = getViewModel().getProductByName(productName);
        if (product == null) return;
        getBinding().setProduct(product);
        sizesAdapter.setItems(product.getAvailableSizes());
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.product_detail_fragment;
    }
}
