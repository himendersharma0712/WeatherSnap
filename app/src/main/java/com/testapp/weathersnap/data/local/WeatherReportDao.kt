package com.testapp.weathersnap.data.local


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherReportDao {

    // Streams records automatically ordered by chronological order (most recent first)
    @Query("SELECT * FROM weather_reports ORDER BY timestamp DESC")
    fun getAllReports(): Flow<List<WeatherReport>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insertReport(report: WeatherReport):Long

    @Delete
     fun deleteReport(report: WeatherReport):Int
}