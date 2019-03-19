package br.alexandregpereira.amaro.product.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.alexandregpereira.amaro.model.product.Size
import br.alexandregpereira.amaro.product.R
import br.alexandregpereira.amaro.ui.DefaultAdapter

class SizesAdapter : DefaultAdapter<Size, SizesAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val size = mItems[position]
        holder.root.tag = size
        holder.sizeTextView.text = size.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.size_item, parent, false)
        return ViewHolder(view)
    }

    class ViewHolder(val root: View) : RecyclerView.ViewHolder(root) {
        internal val sizeTextView: TextView = root.findViewById(R.id.sizeTextView)
    }
}