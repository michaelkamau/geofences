package com.mikekamau.geofences.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Geofence(
  @PrimaryKey
  val id: Int,
  val geoPoint: GeoPoint,
  val locationTimestamp: Long,
  val geoPointCaptureTimestamp: Long,
  val transitionName: String,
  val locationId: String,
  val name: String
)