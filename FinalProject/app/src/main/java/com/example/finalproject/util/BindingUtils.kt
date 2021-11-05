package com.example.finalproject.util

import android.net.Uri
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("image")
fun setImageUrl(imageView: ImageView, url: String?) {
    Glide.with(imageView.context)
        .load(Uri.parse("https:$url"))
        .into(imageView)
}

@BindingAdapter("degreeWithSymbol")
fun setDegreeWithSymbol(textView: TextView, text: String) {
    textView.text = text.plus("°")
}

@BindingAdapter("feelsLike")
fun setFeesLike(textView: TextView, text: String) {
    textView.text = "Feels like ".plus(text).plus("°")
}

@BindingAdapter("feelsLikeNewLine")
fun setFeesLikeNewLine(textView: TextView, text: String) {
    textView.text = "Feels like\n".plus(text).plus("°")
}