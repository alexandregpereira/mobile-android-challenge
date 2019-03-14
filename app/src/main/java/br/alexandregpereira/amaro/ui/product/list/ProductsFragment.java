package br.alexandregpereira.amaro.ui.product.list;


import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import br.alexandregpereira.amaro.R;
import br.alexandregpereira.amaro.databinding.ProductsFragmentBinding;
import br.alexandregpereira.amaro.exception.ConnectionError;
import br.alexandregpereira.amaro.extension.ViewExtensionKt;
import br.alexandregpereira.amaro.model.product.ProductContract;
import br.alexandregpereira.amaro.ui.Navigator;
import br.alexandregpereira.amaro.ui.OnBackPressed;
import br.alexandregpereira.amaro.ui.product.ProductFragment;
import br.alexandregpereira.amaro.ui.product.detail.ProductDetailFragment;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class ProductsFragment extends ProductFragment<ProductsFragmentBinding> implements OnBackPressed {

    private final ProductsAdapter adapter = new ProductsAdapter();
    private final Handler handler = new Handler();
    private boolean scrollToTop = false;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getBinding().productRecyclerView.setHasFixedSize(true);
        getBinding().productRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        getBinding().productRecyclerView.setAdapter(adapter);

        getBinding().toolbar.inflateMenu(R.menu.home);
        getBinding().toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                openDrawerLayout();
                return false;
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        adapter.setListener(new Function1<ProductContract, Unit>() {
            @Override
            public Unit invoke(@NonNull ProductContract productContract) {
                navigateToProductDetail(productContract);
                return null;
            }
        });

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

        getBinding().drawerContainer.orderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                handleOrderRadioGroupCheckedChange(checkedId);
            }
        });

        getBinding().drawerContainer.onSaleCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                filterBy(ProductsFilter.ON_SALE, isChecked);
            }
        });

        getBinding().drawerContainer.discountCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                filterBy(ProductsFilter.DISCOUNT, isChecked);
            }
        });

        getViewModel().getProductsLiveData().observe(this, new Observer<List<ProductContract>>() {
            @Override
            public void onChanged(@Nullable List<ProductContract> productContracts) {
                if (productContracts == null) return;
                setProducts(productContracts);
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

    @Override
    public boolean onBackPressed() {
        if (isDrawerLayoutOpen()) {
            closeDrawerLayout();
            return true;
        }

        return false;
    }

    @Override
    public void onDestroyView() {
        scrollToTop = false;
        super.onDestroyView();
    }

    private void navigateToProductDetail(@NonNull ProductContract productContract) {
        FragmentActivity activity = getActivity();
        if (activity instanceof Navigator) {
            ((Navigator) activity).navigateTo(ProductDetailFragment.newInstance(productContract.getName()));
        }
    }

    private void setProducts(@NonNull List<ProductContract> products) {
        boolean empty = products.isEmpty();
        ViewExtensionKt.setVisible(getBinding().messageGroup, empty);
        if (empty) getBinding().errorTextView.setText(R.string.products_empty);

        adapter.setItems(products);

        if (scrollToTop) {
            scrollToTop = false;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getBinding().productRecyclerView.smoothScrollToPosition(0);
                }
            }, 400);
        }
    }

    private void handleOrderRadioGroupCheckedChange(int checkedId) {
        switch (checkedId) {
            case R.id.priceLowHighRadio:
                orderBy(ProductsOrder.LOW_HIGH);
                break;
            case R.id.priceHighLowRadio:
                orderBy(ProductsOrder.HIGH_LOW);
                break;
            case R.id.anyRadio:
                orderBy(ProductsOrder.ANY);
                break;
        }
    }

    private void filterBy(@NonNull ProductsFilter filter, boolean isChecked) {
        scrollToTop = isChecked ? getViewModel().filterBy(filter) : getViewModel().removeFilter(filter);
    }

    private void orderBy(@NonNull ProductsOrder order) {
        scrollToTop = true;
        closeDrawerLayout();
        getViewModel().orderBy(order);
    }

    private void openDrawerLayout() {
        getBinding().drawerLayout.openDrawer(GravityCompat.END, true);
    }

    private void closeDrawerLayout() {
        getBinding().drawerLayout.closeDrawer(GravityCompat.END);
    }

    private boolean isDrawerLayoutOpen() {
        return getBinding().drawerLayout.isDrawerOpen(GravityCompat.END);
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
