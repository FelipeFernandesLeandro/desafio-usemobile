package com.felipefernandes.desafiousemobile.extensions

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.felipefernandes.desafiousemobile.R
import com.felipefernandes.desafiousemobile.config.Constant.delayToRetryApiCall

fun ImageView.loadImage(url: String?, circle: Boolean = false) {
    val requestOptions = RequestOptions()
        .error(R.drawable.glideError)
        .skipMemoryCache(false)
        .dontAnimate()
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        .timeout(delayToRetryApiCall.toInt())

    if (circle) requestOptions.circleCrop()

    Glide.with(context)
        .load(url)
        .apply(requestOptions)
        .into(this)
}
