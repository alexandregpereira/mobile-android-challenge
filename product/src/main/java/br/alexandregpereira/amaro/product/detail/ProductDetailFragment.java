package br.alexandregpereira.amaro.product.detail;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import br.alexandregpereira.amaro.extension.ImageViewExtensionKt;
import br.alexandregpereira.amaro.extension.ViewExtensionKt;
import br.alexandregpereira.amaro.model.product.ProductContract;
import br.alexandregpereira.amaro.product.ProductFragment;
import br.alexandregpereira.amaro.product.R;

public class ProductDetailFragment extends ProductFragment {

    private static final String CODE_COLOR_KEY = "CODE_COLOR_KEY";

    private ViewHolder viewHolder;
    private String productCodeColor;
    private final SizesAdapter sizesAdapter = new SizesAdapter();

    public static ProductDetailFragment newInstance(@NonNull String productCodeColor) {
        Bundle bundle = new Bundle();
        bundle.putString(CODE_COLOR_KEY, productCodeColor);
        ProductDetailFragment fragment = new ProductDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        productCodeColor = getArguments().getString(CODE_COLOR_KEY);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewHolder = new ViewHolder(view);
        viewHolder.sizesRecycler.setHasFixedSize(true);
        viewHolder.sizesRecycler.setAdapter(sizesAdapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewHolder.closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentActivity activity = getActivity();
                if (activity != null) activity.onBackPressed();
            }
        });

        ProductContract product = getViewModel().getProductByCodeColor(productCodeColor);
        if (product == null) return;
        ImageViewExtensionKt.loadImageUrl(viewHolder.imageView, product.getImage());

        viewHolder.titleTextView.setText(product.getName());
        viewHolder.regularTextView.setText(product.getRegularPrice());
        viewHolder.actualPriceTextView.setText(product.getActualPrice());
        viewHolder.installmentsTextView.setText(product.getInstallments());
        viewHolder.discountTextView.setText(product.getDiscountPercentageOff());

        ViewExtensionKt.setStrikeThru(viewHolder.regularTextView, true);
        ViewExtensionKt.setVisible(viewHolder.notAvailableLabelTextView, !product.getOnSale());
        ViewExtensionKt.setVisible(viewHolder.group, product.hasDiscount());
        ViewExtensionKt.setVisible(viewHolder.sizesGroup, !product.isAvailableSizesEmpty());
        sizesAdapter.setItems(product.getAvailableSizes());
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.product_detail_fragment;
    }
    
    private static class ViewHolder {

        final RecyclerView sizesRecycler;
        final ImageButton closeButton;
        final ImageView imageView;
        final TextView titleTextView;
        final TextView regularTextView;
        final TextView actualPriceTextView;
        final TextView notAvailableLabelTextView;
        final TextView installmentsTextView;
        final TextView discountTextView;
        final Group group;
        final Group sizesGroup;

        ViewHolder(View view) {
            sizesRecycler = view.findViewById(R.id.sizesRecycler);
            closeButton = view.findViewById(R.id.closeButton);
            imageView = view.findViewById(R.id.imageView);
            titleTextView = view.findViewById(R.id.titleTextView);
            regularTextView = view.findViewById(R.id.regularTextView);
            actualPriceTextView = view.findViewById(R.id.actualPriceTextView);
            discountTextView = view.findViewById(R.id.discountTextView);
            notAvailableLabelTextView = view.findViewById(R.id.notAvailableLabelTextView);
            installmentsTextView = view.findViewById(R.id.installmentsTextView);
            group = view.findViewById(R.id.group);
            sizesGroup = view.findViewById(R.id.sizesGroup);
        }
    }
}
