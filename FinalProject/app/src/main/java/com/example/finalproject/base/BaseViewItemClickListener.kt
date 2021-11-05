package com.example.finalproject.base

import androidx.annotation.IdRes

interface BaseViewItemClickListener<T> {
    fun onItemClicked(clickedObject: T, @IdRes id: Int = 0)
}