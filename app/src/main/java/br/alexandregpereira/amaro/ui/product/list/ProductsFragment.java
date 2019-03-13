package br.alexandregpereira.amaro.ui.product.list;


import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import br.alexandregpereira.amaro.R;
import br.alexandregpereira.amaro.databinding.ProductsFragmentBinding;
import br.alexandregpereira.amaro.exception.ConnectionError;
import br.alexandregpereira.amaro.extension.ViewExtensionKt;
import br.alexandregpereira.amaro.model.product.ProductContract;
import br.alexandregpereira.amaro.ui.product.ProductFragment;

public class ProductsFragment extends ProductFragment<ProductsFragmentBinding> {

    private final ProductsAdapter adapter = new ProductsAdapter();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getBinding().productRecyclerView.setHasFixedSize(true);
        getBinding().productRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        getBinding().productRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getBinding().swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getViewModel().refresh();
            }
        });

        getBinding().tryAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getViewModel().refresh();
            }
        });

        getViewModel().getProductsLiveData().observe(this, new Observer<List<ProductContract>>() {
            @Override
            public void onChanged(@Nullable List<ProductContract> productContracts) {
                if (productContracts == null) return;
                boolean empty = productContracts.isEmpty();
                ViewExtensionKt.setVisible(getBinding().messageGroup, empty);
                if (empty) getBinding().errorTextView.setText(R.string.products_empty);

                adapter.setItems(productContracts);
            }
        });

        getViewModel().getErrorLiveData().observe(this, new Observer<ConnectionError>() {
            @Override
            public void onChanged(@Nullable ConnectionError connectionError) {
                if (connectionError == null) return;
                handleConnectionError(connectionError);
            }
        });

        getViewModel().getLoadingLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean loading) {
                getBinding().swipe.setRefreshing(loading == null ? false : loading);
            }
        });
    }

    private void handleConnectionError(@NonNull ConnectionError connectionError) {
        switch (connectionError) {
            case NO_INTERNET:
                showErrorMessage(R.string.no_internet_error);
                break;
            case NO_CONNECTION:
                showErrorMessage(R.string.no_connection_error);
                break;
            case TIMEOUT:
                showErrorMessage(R.string.timeout_error);
                break;
            case UNKNOWN:
                showErrorMessage(R.string.unknown_error);
                break;
        }
    }

    private void showErrorMessage(@StringRes int message) {
        if (getViewModel().isLiveDataEmpty()) {
            ViewExtensionKt.setVisible(getBinding().messageGroup, true);
            getBinding().errorTextView.setText(message);
            return;
        }

        ViewExtensionKt.setVisible(getBinding().messageGroup, false);
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.products_fragment;
    }
}
