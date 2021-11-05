package com.example.finalproject.ui.weatherapp.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "RESULTS")
data class ResultCurrent(
    @PrimaryKey @Embedded val location: Location,
    @Embedded val current: Current,
    @ColumnInfo(name = "isCelsiusSelected") var isCelsiusSelected: Boolean
)