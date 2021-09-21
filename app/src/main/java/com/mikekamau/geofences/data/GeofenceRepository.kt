package com.mikekamau.geofences.data

import com.mikekamau.geofences.data.db.GeofenceDao
import com.mikekamau.geofences.models.GeofenceModel
import kotlinx.coroutines.flow.Flow

class GeofenceRepository(private val geofenceDao: GeofenceDao) {

  suspend fun insert(geofenceModel: GeofenceModel) {
    geofenceDao.insertAll(geofenceModel)
  }

  val allGeofences: Flow<List<GeofenceModel>> = geofenceDao.getAll()
}