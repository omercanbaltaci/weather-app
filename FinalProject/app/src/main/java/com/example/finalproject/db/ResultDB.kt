package com.example.finalproject.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.finalproject.ui.weatherapp.model.ResultCurrent

@Database(entities = [ResultCurrent::class], version = 1)
abstract class ResultDB : RoomDatabase() {
    abstract fun weatherDao(): ResultDAO
}