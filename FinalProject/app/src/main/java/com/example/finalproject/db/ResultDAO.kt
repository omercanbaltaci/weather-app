package com.example.finalproject.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.finalproject.ui.weatherapp.model.ResultCurrent

@Dao
interface ResultDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResult(resultCurrent: ResultCurrent)

    @Query("SELECT * FROM RESULTS")
    suspend fun fetchResults(): List<ResultCurrent>

    @Query("SELECT * FROM RESULTS WHERE name = :name AND region = :region")
    suspend fun fetchSingleResult(name: String, region: String): ResultCurrent?

    @Query("DELETE FROM RESULTS WHERE name = :name AND region = :region")
    suspend fun deleteResult(name: String, region: String): Int
}