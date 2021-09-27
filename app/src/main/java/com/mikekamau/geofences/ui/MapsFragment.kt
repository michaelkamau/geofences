package com.mikekamau.geofences.ui

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.mikekamau.geofences.R
import com.mikekamau.geofences.data.GeofenceUtils
import com.mikekamau.geofences.databinding.FragmentMapsBinding

class MapsFragment : Fragment() {

  lateinit var binding: FragmentMapsBinding
  private lateinit var map: GoogleMap
  private lateinit var geofencingClient: GeofencingClient
  private lateinit var geofenceUtils: GeofenceUtils

  private val onMapReadyCallback = OnMapReadyCallback { googleMap ->
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * In this case, we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to
     * install it inside the SupportMapFragment. This method will only be triggered once the
     * user has installed Google Play services and returned to the app.
     */

    map = googleMap
    map.setOnMapLongClickListener(onMapLongClickListener)

    val home = getUserCurrentLocation()
    map.addMarker(MarkerOptions().position(home).title("Home"))
    map.animateCamera(
      CameraUpdateFactory.newLatLngZoom(home, 17.5f),
      500, null
    )
  }

  private val onMapLongClickListener = GoogleMap.OnMapLongClickListener { position ->
    addMarker(position)
    addCircle(position, 100.0)
  }


  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    binding = FragmentMapsBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
    mapFragment?.getMapAsync(onMapReadyCallback)
    geofencingClient = LocationServices.getGeofencingClient(requireActivity())
    geofenceUtils = GeofenceUtils(requireActivity())

    binding.btnContinue.setOnClickListener {
      AddGeofenceFragment.newInstance().show(childFragmentManager, AddGeofenceFragment.TAG)
    }

  }


  private fun addMarker(position: LatLng) {
    map.clear()
    map.addMarker(MarkerOptions().apply {
      position(position)
      draggable(false)
    })
  }

  private fun addCircle(position: LatLng, radiusMeters: Double) {
    map.addCircle(CircleOptions().apply {
      center(position)
      radius(radiusMeters)
      strokeWidth(4f)
      strokeColor(Color.RED)
      fillColor(Color.CYAN)
    })
  }

  private fun getUserCurrentLocation(): LatLng {
    // todo: add proper impl
    return LatLng(-1.2811374954430768, 36.66097762870249);
  }

}