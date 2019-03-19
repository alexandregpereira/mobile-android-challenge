package br.alexandregpereira.amaro.extension

import android.graphics.Paint
import android.view.View
import android.widget.TextView


fun View.setVisible(visibility: Boolean = false) {
    this.visibility = if(visibility) View.VISIBLE else View.GONE
}

fun TextView.setStrikeThru(value: Boolean) {
    if (value) this.paintFlags = this.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
}