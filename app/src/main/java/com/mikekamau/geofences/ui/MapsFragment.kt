package com.mikekamau.geofences.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.mikekamau.geofences.GeofencesApplication
import com.mikekamau.geofences.R
import com.mikekamau.geofences.data.GeofenceUtils
import com.mikekamau.geofences.databinding.FragmentMapsBinding
import java.util.*

class MapsFragment() : Fragment() {

  lateinit var binding: FragmentMapsBinding
  private lateinit var map: GoogleMap
  private lateinit var geofencingClient: GeofencingClient
  private lateinit var geofenceUtils: GeofenceUtils

  private val viewModel: GeofenceViewModel by activityViewModels {
    GeofenceViewModelFactory(GeofencesApplication.getInstance().repository)
  }

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
    viewModel.selectedPosition = position
    if (viewModel.radius.get() != null) {
      updateGeofenceMarker(position, viewModel.getRadiusDouble())
    } else {
      updateGeofenceMarker(position, GeofenceUtils.DEFAULT_GEOFENCE_RADIUS)
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    geofenceUtils = GeofenceUtils(requireActivity())
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
    binding.viewModel = viewModel

    binding.btnContinue.setOnClickListener {
      AddGeofenceFragment.newInstance().show(childFragmentManager, AddGeofenceFragment.TAG)
    }

    observeGeofenceFieldUpdates()
  }

  /**
   * Updates the Geofence marker and circle on map after user has updated
   * properties on AddGeofenceFragment
   */
  private fun observeGeofenceFieldUpdates() {
    viewModel.getGeofenceUpdate().observe(viewLifecycleOwner) { isUpdated ->
      if (isUpdated != null) {
        if (isUpdated) {
          viewModel.selectedPosition?.let { position ->
            updateGeofenceMarker(position, viewModel.getRadiusDouble())
          }
          viewModel.setGeofenceUpdated(null)
        }
      }

    }
  }

  @SuppressLint("MissingPermission")
  fun addGeofence(latLng: LatLng, radius: Float) {
    val geofence = geofenceUtils.createGeofence(
      UUID.randomUUID().toString(),
      latLng,
      viewModel.getRadiusFloat(),
      GeofenceUtils.DEFAULT_EXPIRY_MILLIS,
      viewModel.getInitialTransitionTrigger(),
      GeofenceUtils.DEFAULT_LOITER_DELAY_MILLIS
    )
    val initialTrigger = viewModel.getInitialTransitionTrigger()
    val geofenceRequest = geofenceUtils.getGeofencingRequest(geofence, initialTrigger)
    val geofencePendingIntent = geofenceUtils.geofencePendingIntent
    geofencingClient.addGeofences(geofenceRequest, geofencePendingIntent)
      .addOnSuccessListener {
        Log.d(TAG, "Created geofence $geofence")
      }

      .addOnFailureListener {
        geofenceUtils.geofenceError(it)
      }
  }

  private fun updateGeofenceMarker(position: LatLng, radius: Double) {
    addMarker(position)
    addCircle(position, radius)
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
      strokeColor(resources.getColor(R.color.summer_sky, requireActivity().theme))
      fillColor(resources.getColor(R.color.cornflower, requireActivity().theme))
    })
  }

  private fun getUserCurrentLocation(): LatLng {
    // todo: add proper impl
    return LatLng(-1.2811374954430768, 36.66097762870249);
  }

  companion object {

    fun getInstance(context: Context): MapsFragment {
      return MapsFragment(context)
    }

    const val TAG = "MapsFragment"
  }

  constructor(context: Context) : this() {
    geofenceUtils = GeofenceUtils(context)
  }

}