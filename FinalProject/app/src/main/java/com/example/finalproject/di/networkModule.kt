package com.example.finalproject.di

import com.example.finalproject.network.WeatherAPI
import com.example.finalproject.util.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    factory { provideHttpClient() }
    single { provideMovieApi(get()) }
    single { provideRetrofit(get()) }
}

fun provideHttpClient(): OkHttpClient {
    val logging = HttpLoggingInterceptor().apply {
        setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    return OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()
}

fun provideRetrofit(httpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(httpClient)
        .build()
}

fun provideMovieApi(retrofit: Retrofit): WeatherAPI {
    return retrofit.create(WeatherAPI::class.java)
}