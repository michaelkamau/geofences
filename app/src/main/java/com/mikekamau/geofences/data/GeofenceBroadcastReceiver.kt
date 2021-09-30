package com.mikekamau.geofences.data

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class GeofenceBroadcastReceiver : BroadcastReceiver() {

  override fun onReceive(context: Context, intent: Intent) {
    // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
//    TODO("GeofenceBroadcastReceiver.onReceive() is not implemented")
    Toast.makeText(context, "Geofence triggered ...", Toast.LENGTH_SHORT).show()
  }
}