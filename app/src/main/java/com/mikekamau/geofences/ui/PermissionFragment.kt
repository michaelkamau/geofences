package com.mikekamau.geofences.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.mikekamau.geofences.R

class PermissionFragment : Fragment() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(
        requireContext(),
        Manifest.permission.ACCESS_BACKGROUND_LOCATION
      )
    ) {
      navigateToMaps()
    } else {
      // Permission to access the location is missing. Show rationale and request permission
      requestPermissionLauncher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
    }

  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_permission, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    view.findViewById<MaterialButton>(R.id.btn_ok).setOnClickListener {
      navigateWithPermissionCheck()
    }
  }

  private fun navigateWithPermissionCheck() {
    when {
      ContextCompat.checkSelfPermission(
        requireContext(),
        Manifest.permission.ACCESS_BACKGROUND_LOCATION
      ) == PackageManager.PERMISSION_GRANTED -> {
        // You can use the API that requires the permission.
        navigateToMaps()
      }
      shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_BACKGROUND_LOCATION) -> {
        // In an educational UI, explain to the user why your app requires this
        // permission for a specific feature to behave as expected. In this UI,
        // include a "cancel" or "no thanks" button that allows the user to
        // continue using your app without granting the permission.
        Toast.makeText(
          requireActivity(),
          "XXXX Please grant the application Location permission to enjoy full functionality",
          Toast.LENGTH_SHORT
        )
          .show()
      }
      else -> {
        // You can directly ask for the permission.
        // The registered ActivityResultCallback gets the result of this request.
        requestPermissionLauncher.launch(
          Manifest.permission.ACCESS_BACKGROUND_LOCATION
        )
      }
    }

  }

  // Register the permissions callback, which handles the user's response to the
  // system permissions dialog.
  private val requestPermissionLauncher =
    registerForActivityResult(
      ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
      when {
        isGranted -> {
          navigateToMaps()
        }
        else -> {
          // Explain to the user that the feature is unavailable because the
          // features requires a permission that the user has denied. At the
          // same time, respect the user's decision. Don't link to system
          // settings in an effort to convince the user to change their
          // decision.

          // todo: should show need for permission here

          Toast.makeText(
            requireActivity(),
            "Please grant the application Location permission to enjoy full functionality",
            Toast.LENGTH_SHORT
          )
            .show()

        }
      }
    }

  private fun navigateToMaps() {
    findNavController().navigate(R.id.dest_maps, null)
  }


  companion object {

    const val TAG = "PermissionFragment"

    @JvmStatic
    fun newInstance() = PermissionFragment()
  }
}