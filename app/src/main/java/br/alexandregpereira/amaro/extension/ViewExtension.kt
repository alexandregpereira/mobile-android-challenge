package br.alexandregpereira.amaro.extension

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("visible")
fun View.setVisible(visibility: Boolean = false) {
    this.visibility = if(visibility) View.VISIBLE else View.GONE
}