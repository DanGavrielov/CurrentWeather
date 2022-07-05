package com.giniapps.currentweather.data.cache.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.giniapps.currentweather.data.cache.room.daos.CurrentLocationDao
import com.giniapps.currentweather.data.cache.room.daos.LocationDao
import com.giniapps.currentweather.data.cache.room.daos.WeatherDetailsDao
import com.giniapps.currentweather.data.cache.room.entities.CurrentLocationEntity
import com.giniapps.currentweather.data.cache.room.entities.LocationEntity
import com.giniapps.currentweather.data.cache.room.entities.WeatherDetailsEntity

@Database(
    entities = [
        WeatherDetailsEntity::class,
        LocationEntity::class,
        CurrentLocationEntity::class
    ], version = 1, exportSchema = false
)
abstract class WeatherDatabase: RoomDatabase() {
    abstract fun weatherDetailsDao(): WeatherDetailsDao
    abstract fun locationDao(): LocationDao
    abstract fun currentLocationDao(): CurrentLocationDao

    companion object {
        @Volatile
        private var instance: WeatherDatabase? = null

        fun getDatabase(context: Context): WeatherDatabase {
            return instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    WeatherDatabase::class.java,
                    "local_db"
                ).build()
                    .also { instance = it }
            }
        }
    }
}