package com.example.finalproject

import androidx.annotation.StringDef

class Outcome<T>(@Status val status: String, val data: T?, val error: Exception?) {
    companion object {
        @Retention(AnnotationRetention.SOURCE)
        @StringDef(SUCCESS, ERROR, LOADING)
        annotation class Status

        const val SUCCESS = "success"
        const val ERROR = "error"
        const val LOADING = "loading"

        fun <T> success(data: T?): Outcome<T> {
            return Outcome(SUCCESS, data, null)
        }

        fun <T> error(exception: Exception): Outcome<T> {
            return Outcome(ERROR, null, exception)
        }

        fun <T> loading(data: T? = null): Outcome<T> {
            return Outcome(LOADING, data, null)
        }
    }
}