package com.mikekamau.geofences.ui

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.mikekamau.geofences.GeofencesApplication
import com.mikekamau.geofences.R
import com.mikekamau.geofences.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

  // Register the permissions callback, which handles the user's response to the
  // system permissions dialog.
  private val requestPermissionsLauncher: ActivityResultLauncher<Array<String>> =
    registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions())
    { permissions ->
      permissions.entries.forEach {
        if (it.value) {
          // this permission is granted
          Log.d(TAG, "${it.key} is granted")
        } else {
          // this permission is not granted
          Log.d(TAG, "${it.key} is not granted")
          RequestPermissionDialog(null).show(requireActivity().supportFragmentManager, "dialog")
        }
      }
    }

  private val geofencesViewModel: GeofenceViewModel by activityViewModels {
    GeofenceViewModelFactory(GeofencesApplication.getInstance().repository)
  }

  lateinit var binding: FragmentDashboardBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    binding = FragmentDashboardBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)


    geofencesViewModel.allGeoFences.observe(viewLifecycleOwner) { geofences ->
      if (geofences.isEmpty()) {
        binding.tvWarningNoGeofence.visibility = View.VISIBLE
      } else {
        binding.tvWarningNoGeofence.visibility = View.GONE
      }
    }

    binding.btnAddGeofence.setOnClickListener {
      navigateWithPermissionCheck()
    }
  }

  private fun navigateWithPermissionCheck() {
    when {
      ContextCompat.checkSelfPermission(
        requireActivity(),
        Manifest.permission.ACCESS_BACKGROUND_LOCATION
      ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
        requireActivity(),
        Manifest.permission.ACCESS_FINE_LOCATION
      ) == PackageManager.PERMISSION_GRANTED -> {
        navigateToMaps()
      }

      shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_BACKGROUND_LOCATION) -> {
        // show dialog explaining need for this permission
        RequestPermissionDialog(requestPermissionsLauncher).show(
          requireActivity().supportFragmentManager,
          "dialog"
        )
      }

      shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
        // show dialog explaining need for this permission
        RequestPermissionDialog(requestPermissionsLauncher).show(
          requireActivity().supportFragmentManager,
          "dialog"
        )
      }

      else -> {
        requestPermissionsLauncher.launch(
          arrayOf(
            Manifest.permission.ACCESS_BACKGROUND_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
          )
        )
      }

    }
  }

  /**
   * This dialog explains to the user why we need access to Location permissions
   */
  class RequestPermissionDialog(val permissionLauncher: ActivityResultLauncher<Array<String>>?) :
    DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
      return AlertDialog.Builder(activity)
        .setMessage(R.string.label_permission_request)
        .setPositiveButton(android.R.string.ok) { dialog, which ->
          // Do not finish the Activity while requesting permission.
          permissionLauncher?.launch(
            arrayOf(
              Manifest.permission.ACCESS_BACKGROUND_LOCATION,
              Manifest.permission.ACCESS_FINE_LOCATION
            )
          )
        }
        .setNegativeButton(android.R.string.cancel, null)
        .create()
    }
  }

  private fun navigateToMaps() {
    findNavController().navigate(R.id.dest_maps, null)
  }

  companion object {

    private val TAG = DashboardFragment.javaClass.simpleName

    @JvmStatic
    fun newInstance() = DashboardFragment()
  }
}
