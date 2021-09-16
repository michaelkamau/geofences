package com.mikekamau.geofences.models

data class Geofence(
  val geoPoint: GeoPoint,
  val locationTimestamp: Long,
  val geoPointCaptureTimestamp: Long,
  val transitionName: String,
  val locationId: String,
  val name: String
)