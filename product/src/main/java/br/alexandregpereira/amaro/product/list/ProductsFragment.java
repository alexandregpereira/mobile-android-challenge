package br.alexandregpereira.amaro.product.list;


import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.Group;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.transition.Fade;
import br.alexandregpereira.amaro.exception.ConnectionError;
import br.alexandregpereira.amaro.extension.ViewExtensionKt;
import br.alexandregpereira.amaro.model.product.ProductContract;
import br.alexandregpereira.amaro.product.DetailsTransition;
import br.alexandregpereira.amaro.product.ProductFragment;
import br.alexandregpereira.amaro.product.R;
import br.alexandregpereira.amaro.product.detail.ProductDetailFragment;
import br.alexandregpereira.amaro.ui.Navigator;
import br.alexandregpereira.amaro.ui.OnBackPressed;
import br.alexandregpereira.amaro.ui.product.list.ProductsFilter;
import br.alexandregpereira.amaro.ui.product.list.ProductsOrder;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class ProductsFragment extends ProductFragment implements OnBackPressed {

    private final ProductsAdapter adapter = new ProductsAdapter();
    private final Handler handler = new Handler();
    private boolean scrollToTop = false;
    private ViewHolder viewHolder;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewHolder = new ViewHolder(view);
        viewHolder.productRecyclerView.setHasFixedSize(true);
        viewHolder.productRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        viewHolder.productRecyclerView.setAdapter(adapter);

        viewHolder.toolbar.inflateMenu(R.menu.home);
        viewHolder.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
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

        viewHolder.swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getViewModel().refresh();
            }
        });

        viewHolder.tryAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getViewModel().refresh();
            }
        });

        viewHolder.orderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                handleOrderRadioGroupCheckedChange(checkedId);
            }
        });

        viewHolder.onSaleCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                filterBy(ProductsFilter.ON_SALE, isChecked);
            }
        });

        viewHolder.discountCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
                viewHolder.swipe.setRefreshing(loading == null ? false : loading);
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
            ProductDetailFragment detailFragment = ProductDetailFragment.newInstance(productContract.getCodeColor());

            detailFragment.setSharedElementEnterTransition(new DetailsTransition());
            detailFragment.setEnterTransition(new Fade());
            setExitTransition(new Fade());
            detailFragment.setSharedElementReturnTransition(new Fade());

            int index = adapter.getItems().indexOf(productContract);
            RecyclerView.LayoutManager layoutManager = viewHolder.productRecyclerView.getLayoutManager();

            FragmentTransaction transaction = ((Navigator) activity).navigateTo(detailFragment);
            if (layoutManager == null) {
                transaction.commit();
                return;
            }

            View rootView = layoutManager.findViewByPosition(index);
            if (rootView == null) {
                transaction.commit();
                return;
            }

            View view = rootView.findViewById(R.id.imageView);
            if (view == null) {
                transaction.commit();
                return;
            }

            transaction.addSharedElement(view, "sharedImage").commit();
        }
    }

    private void setProducts(@NonNull List<ProductContract> products) {
        boolean empty = products.isEmpty();
        ViewExtensionKt.setVisible(viewHolder.messageGroup, empty);
        ViewExtensionKt.setVisible(viewHolder.tryAgainButton, empty);
        ViewExtensionKt.setVisible(viewHolder.errorTextView, empty);
        if (empty) viewHolder.errorTextView.setText(R.string.products_empty);

        adapter.setItems(products);

        if (scrollToTop) {
            scrollToTop = false;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    viewHolder.productRecyclerView.smoothScrollToPosition(0);
                }
            }, 400);
        }
    }

    private void handleOrderRadioGroupCheckedChange(int checkedId) {
        if (checkedId == R.id.priceLowHighRadio)
            orderBy(ProductsOrder.LOW_HIGH);
        else if (checkedId == R.id.priceHighLowRadio)
            orderBy(ProductsOrder.HIGH_LOW);
        else if (checkedId == R.id.anyRadio)
            orderBy(ProductsOrder.ANY);
    }

    private void filterBy(@NonNull ProductsFilter filter, boolean isChecked) {
        scrollToTop = isChecked ? getViewModel().filterBy(filter) : getViewModel().removeFilter(filter);
    }

    private void orderBy(@NonNull ProductsOrder order) {
        if (!this.isResumed()) return;
        scrollToTop = true;
        closeDrawerLayout();
        getViewModel().orderBy(order);
    }

    private void openDrawerLayout() {
        viewHolder.drawerLayout.openDrawer(GravityCompat.END, true);
    }

    private void closeDrawerLayout() {
        viewHolder.drawerLayout.closeDrawer(GravityCompat.END);
    }

    private boolean isDrawerLayoutOpen() {
        return viewHolder.drawerLayout.isDrawerOpen(GravityCompat.END);
    }

    private void handleConnectionError(@NonNull ConnectionError connectionError) {
        switch (connectionError) {
            case NO_INTERNET:
                showErrorMessage(br.alexandregpereira.amaro.R.string.no_internet_error);
                break;
            case NO_CONNECTION:
                showErrorMessage(br.alexandregpereira.amaro.R.string.no_connection_error);
                break;
            case TIMEOUT:
                showErrorMessage(br.alexandregpereira.amaro.R.string.timeout_error);
                break;
            case UNKNOWN:
                showErrorMessage(br.alexandregpereira.amaro.R.string.unknown_error);
                break;
        }
    }

    private void showErrorMessage(@StringRes int message) {
        if (getViewModel().isLiveDataEmpty()) {
            ViewExtensionKt.setVisible(viewHolder.messageGroup, true);
            ViewExtensionKt.setVisible(viewHolder.errorTextView, true);
            ViewExtensionKt.setVisible(viewHolder.tryAgainButton, true);
            viewHolder.errorTextView.setText(message);
            return;
        }

        ViewExtensionKt.setVisible(viewHolder.messageGroup, false);
        ViewExtensionKt.setVisible(viewHolder.tryAgainButton, false);
        ViewExtensionKt.setVisible(viewHolder.errorTextView, false);
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.products_fragment;
    }

    private static class ViewHolder {

        final RecyclerView productRecyclerView;
        final Toolbar toolbar;
        final SwipeRefreshLayout swipe;
        final DrawerLayout drawerLayout;
        final TextView errorTextView;
        final Group messageGroup;
        final Button tryAgainButton;
        final RadioGroup orderRadioGroup;
        final CheckBox onSaleCheckBox;
        final CheckBox discountCheckBox;

        ViewHolder(View view) {
            productRecyclerView = view.findViewById(R.id.productRecyclerView);
            toolbar = view.findViewById(R.id.toolbar);
            swipe = view.findViewById(R.id.swipe);
            drawerLayout = view.findViewById(R.id.drawerLayout);
            errorTextView = view.findViewById(R.id.errorTextView);
            messageGroup = view.findViewById(R.id.messageGroup);
            tryAgainButton = view.findViewById(R.id.tryAgainButton);
            orderRadioGroup = view.findViewById(R.id.orderRadioGroup);
            onSaleCheckBox = view.findViewById(R.id.onSaleCheckBox);
            discountCheckBox = view.findViewById(R.id.discountCheckBox);
        }
    }
}
