package com.mikekamau.geofences.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Geofence(
  @PrimaryKey
  var id: Int,
  /* geopoint fields are added to the Geofence table*/
  @Embedded
  var geoPoint: GeoPoint,
  var locationTimestamp: Long,
  var geoPointCaptureTimestamp: Long,
  var transitionName: String,
  var locationId: String,
  var name: String
)