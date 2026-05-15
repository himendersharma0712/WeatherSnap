package com.testapp.weathersnap.data.local


import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [WeatherReport::class], version = 1, exportSchema = false)
abstract class WeatherSnapDatabase : RoomDatabase() {
    abstract fun weatherReportDao(): WeatherReportDao
}