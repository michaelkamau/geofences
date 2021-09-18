package com.mikekamau.geofences.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.mikekamau.geofences.models.Geofence
import kotlinx.coroutines.flow.Flow

@Dao
interface GeofenceDao {

  @Insert
  suspend fun insertAll(vararg geofences: Geofence)

  @Query("SELECT * FROM Geofence ORDER BY id")
  fun getAll(): Flow<List<Geofence>>

  @Delete
  suspend fun delete(vararg geofences: Geofence): Int
}