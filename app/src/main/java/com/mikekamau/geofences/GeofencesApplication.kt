package com.mikekamau.geofences

import android.app.Application
import com.mikekamau.geofences.data.GeofenceRepository
import com.mikekamau.geofences.data.db.GeofenceDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class GeofencesApplication : Application() {

  val applicationScope = CoroutineScope(SupervisorJob())

  val db by lazy { GeofenceDatabase.getInstance(this) }
  val repository by lazy { GeofenceRepository(db.geofenceDao()) }
}