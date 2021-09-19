package com.mikekamau.geofences.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.mikekamau.geofences.GeofencesApplication
import com.mikekamau.geofences.R

class MainActivity : AppCompatActivity() {

  val geofencesViewModel: GeofenceViewModel by viewModels {
    GeofenceViewModelFactory(GeofencesApplication.getInstance().repository)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val navHostFragment =
      supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
    val navController = navHostFragment.navController
  }
}