package com.example.finalproject.di

import androidx.room.Room
import com.example.finalproject.db.ResultDB
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private const val DATABASE_NAME = "WEATHERDB"
val dbModule = module {
    single { Room.databaseBuilder(androidContext(), ResultDB::class.java, DATABASE_NAME).build() }
    single { get<ResultDB>().weatherDao() }
}