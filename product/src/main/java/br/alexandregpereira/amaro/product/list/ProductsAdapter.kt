package br.alexandregpereira.amaro.product.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import br.alexandregpereira.amaro.extension.loadImageUrl
import br.alexandregpereira.amaro.extension.setStrikeThru
import br.alexandregpereira.amaro.extension.setVisible
import br.alexandregpereira.amaro.model.product.ProductContract
import br.alexandregpereira.amaro.product.R
import br.alexandregpereira.amaro.ui.DefaultAdapter

class ProductsAdapter : DefaultAdapter<ProductContract, ProductsAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = mItems[position]
        holder.root.tag = product

        ViewCompat.setTransitionName(holder.imageView, product.codeColor)
        holder.imageView.loadImageUrl(product.image)
        holder.regularTextView.setStrikeThru(true)
        holder.group.setVisible(product.hasDiscount())

        holder.discountTextView.text = product.getDiscountPercentageOff()
        holder.titleTextView.text = product.name
        holder.actualPriceTextView.text = product.actualPrice
        holder.regularTextView.text = product.regularPrice
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
        return ViewHolder(view, listener)
    }

    class ViewHolder(val root: View, onClick: ((ProductContract) -> Unit)?) : RecyclerView.ViewHolder(root) {
        internal val imageView: ImageView = root.findViewById(R.id.imageView)
        internal val discountTextView: TextView = root.findViewById(R.id.discountTextView)
        internal val titleTextView: TextView = root.findViewById(R.id.titleTextView)
        internal val actualPriceTextView: TextView = root.findViewById(R.id.actualPriceTextView)
        internal val regularTextView: TextView = root.findViewById(R.id.regularTextView)
        internal val group: Group = root.findViewById(R.id.group)

        init {
            if (onClick != null) {
                root.setOnClickListener { view ->
                    onClick(view.tag as ProductContract)
                }
            }
        }
    }
}