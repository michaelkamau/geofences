package com.mikekamau.geofences.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.mikekamau.geofences.models.GeofenceModel
import kotlinx.coroutines.flow.Flow

@Dao
interface GeofenceDao {

  @Insert
  suspend fun insertAll(vararg geofenceModels: GeofenceModel)

  @Query("SELECT * FROM GeofenceModel ORDER BY id")
  fun getAll(): Flow<List<GeofenceModel>>

  @Delete
  suspend fun delete(vararg geofenceModels: GeofenceModel): Int
}