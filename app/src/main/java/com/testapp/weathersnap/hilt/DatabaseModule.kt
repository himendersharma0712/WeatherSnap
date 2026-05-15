package com.testapp.weathersnap.hilt


import android.content.Context
import androidx.room.Room
import com.testapp.weathersnap.data.local.WeatherReportDao
import com.testapp.weathersnap.data.local.WeatherSnapDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): WeatherSnapDatabase {
        return Room.databaseBuilder(
            context,
            WeatherSnapDatabase::class.java,
            "weathersnap_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideWeatherReportDao(database: WeatherSnapDatabase): WeatherReportDao {
        return database.weatherReportDao()
    }
}