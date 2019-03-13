package br.alexandregpereira.amaro.extension

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import br.alexandregpereira.amaro.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions


@BindingAdapter("loadImageUrl")
fun ImageView.loadImageUrl(url: String = "") {
    val options = RequestOptions()
        .placeholder(R.drawable.placeholder)

    Glide.with(context)
        .load(url)
        .transition(DrawableTransitionOptions.withCrossFade())
        .apply(options)
        .into(this)
}