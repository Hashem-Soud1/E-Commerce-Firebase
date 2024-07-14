package com.example.e_commerce.ui.common.views

import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.e_commerce.R

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, imageUrl: String?) {
//    val loading = getGlideCircleLoading(view)
//
//    Glide.with(view.context)
//        .load(imageUrl)
//        .placeholder(loading)
//        .diskCacheStrategy(DiskCacheStrategy.ALL)
////        .signature(Object())
//        .transform(CenterCrop(), RoundedCorners(16), )
//        .into(view)


    Glide.with(view.context)
        .load(imageUrl)
        .transform(CenterCrop(), RoundedCorners(16))
        .into(view)
}

fun getGlideCircleLoading(view: ImageView): CircularProgressDrawable {
    return CircularProgressDrawable(view.context).apply {
        strokeWidth = 5f
        centerRadius = 30f
        setColorSchemeColors(ContextCompat.getColor(view.context, R.color.primary_color))
        start()
    }
}