package br.alexandregpereira.amaro.extension

import android.graphics.Paint
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter


@BindingAdapter("visible")
fun View.setVisible(visibility: Boolean = false) {
    this.visibility = if(visibility) View.VISIBLE else View.GONE
}

@BindingAdapter("strikeThru")
fun TextView.setStrikeThru(value: Boolean) {
    if (value) this.paintFlags = this.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
}