package com.example.finalproject.util

import android.animation.ObjectAnimator
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onStart

fun Fragment.showToast(messageToShow: String) {
    Toast.makeText(requireContext(), messageToShow, Toast.LENGTH_SHORT).show()
}

@ExperimentalCoroutinesApi
fun MaterialAutoCompleteTextView.textChanges() = callbackFlow<String?> {
    val listener = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            if (p0!!.isNotEmpty()) trySend(p0.toString())
        }

        override fun afterTextChanged(p0: Editable?) = Unit
    }
    addTextChangedListener(listener)
    awaitClose { removeTextChangedListener(listener) }
}.onStart {
    if (text.toString().length > 3) emit(text.toString())
}


fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

// Smoothly filling up the linear progress indicator in 3000 ms
fun LinearProgressIndicator.smoothProgress(percent: Int) {
    val animation = ObjectAnimator.ofInt(this, "progress", percent)
    animation.duration = DELAY
    animation.interpolator = DecelerateInterpolator()
    animation.start()
}