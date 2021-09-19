package com.mikekamau.geofences.data

import com.mikekamau.geofences.data.db.GeofenceDao
import com.mikekamau.geofences.models.Geofence
import kotlinx.coroutines.flow.Flow

class GeofenceRepository(private val geofenceDao: GeofenceDao) {

  suspend fun insert(geofence: Geofence) {
    geofenceDao.insertAll(geofence)
  }

  val allGeofences: Flow<List<Geofence>> = geofenceDao.getAll()
}