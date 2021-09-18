package com.mikekamau.geofences.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mikekamau.geofences.models.Geofence

@Database(
  entities = [Geofence::class], version = 1
)
abstract class GeofenceDatabase : RoomDatabase() {

  abstract fun geofenceDao(): GeofenceDao

  companion object {

    private const val DB_NAME: String = "geofence_db"
    private var INSTANCE: GeofenceDatabase? = null

    fun getInstance(context: Context): GeofenceDatabase {
      // The GeofenceDatabase should only be initialised once during the application runtime
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          GeofenceDatabase::class.java,
          DB_NAME
        ).build()

        INSTANCE = instance
        instance
      }
    }
  }
}