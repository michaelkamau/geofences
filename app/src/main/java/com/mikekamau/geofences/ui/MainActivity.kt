package com.mikekamau.geofences.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.mikekamau.geofences.GeofencesApplication
import com.mikekamau.geofences.R

class MainActivity : AppCompatActivity() {

  val geofencesViewModel: GeofenceViewModel by viewModels {
    GeofenceViewModelFactory(GeofencesApplication.getInstance().repository)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
  }
}