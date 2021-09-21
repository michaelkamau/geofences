package com.mikekamau.geofences.data

import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.maps.model.LatLng

class GeofenceUtils(base: Context?) : ContextWrapper(base) {

  fun createGeofence(
    requestId: String,
    latLng: LatLng,
    radius: Float,
    expMilli: Long,
    transitionTypes: Int,
    delay: Int
  ): Geofence {
    return Geofence.Builder()
      .setRequestId(requestId)
      .setCircularRegion(latLng.latitude, latLng.longitude, radius)
      .setExpirationDuration(expMilli)
      .setTransitionTypes(transitionTypes)
      .setLoiteringDelay(delay)
      .build()
  }

  fun getGeofencingRequest(geofence: Geofence, trigger: Int): GeofencingRequest {
    return GeofencingRequest.Builder().apply {
      setInitialTrigger(trigger)
      addGeofence(geofence)
    }.build()
  }

  val geofencePendingIntent: PendingIntent by lazy {
    val intent = Intent(this, GeofenceBroadcastReceiver::class.java)
    // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling
    // addGeofences() and removeGeofences().
    PendingIntent.getBroadcast(this, 2002, intent, PendingIntent.FLAG_UPDATE_CURRENT)
  }

}