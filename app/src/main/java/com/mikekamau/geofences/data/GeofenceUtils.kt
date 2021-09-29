package com.mikekamau.geofences.data

import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.util.Log
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
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

  fun getGeofencingRequest(geofence: Geofence, initialTrigger: Int): GeofencingRequest {
    return GeofencingRequest.Builder().apply {
      setInitialTrigger(initialTrigger)
      addGeofence(geofence)
    }.build()
  }

  val geofencePendingIntent: PendingIntent by lazy {
    val intent = Intent(this, GeofenceBroadcastReceiver::class.java)
    // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling
    // addGeofences() and removeGeofences().
    PendingIntent.getBroadcast(this, 2002, intent, PendingIntent.FLAG_UPDATE_CURRENT)
  }

  fun geofenceError(exception: Exception): String {
    Log.e(TAG, "Geofence Error: ${exception.message}")
    if (exception is ApiException) {
      when (exception.statusCode) {
        GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES -> {
          return "Too many geofence have been created"
        }
        GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE -> {
          return "The geofence is not available"
        }
        GeofenceStatusCodes.API_NOT_CONNECTED -> {
          return "Geofence API is not available"
        }
        GeofenceStatusCodes.TIMEOUT -> {
          return "API timed out. Please try again"
        }
        else -> {
          return "Geofence cannot be created. Please try again"
        }
      }
    } else {
      return "Geofence cannot be created. Please try again"
    }
  }


  companion object {

    const val TAG = "GeofenceUtils"

    const val DEFAULT_GEOFENCE_RADIUS = 50.0

    // 14 Days
    const val DEFAULT_EXPIRY_MILLIS = 14 * 24 * 3600L

    const val DEFAULT_TRIGGER = 0

    // 1 Seconds
    // todo: update to a larger value
    const val DEFAULT_LOITER_DELAY_MILLIS = 1 * 60
  }

}