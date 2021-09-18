package com.mikekamau.geofences.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mikekamau.geofences.models.Geofence
import kotlinx.coroutines.flow.Flow

@Dao
interface GeofenceDao {

  @Insert
  suspend fun insertAll(vararg geofences: Geofence)

  @Query("SELECT * FROM Geofence")
  fun getAll(): Flow<List<Geofence>>
}