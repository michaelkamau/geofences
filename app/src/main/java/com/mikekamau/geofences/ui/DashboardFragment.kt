package com.mikekamau.geofences.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.mikekamau.geofences.GeofencesApplication
import com.mikekamau.geofences.R
import com.mikekamau.geofences.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

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
    binding.viewModel = geofencesViewModel

    geofencesViewModel.allGeoFences.observe(viewLifecycleOwner) { geofences ->
      if (geofences.isEmpty()) {
        binding.tvWarningNoGeofence.visibility = View.VISIBLE
      } else {
        binding.tvWarningNoGeofence.visibility = View.GONE
      }
    }

    binding.btnAddGeofence.setOnClickListener {
      navigateToPermissionFrag()
    }
  }


  private fun navigateToPermissionFrag() {
    findNavController().navigate(R.id.dest_permissionFragment, null)
  }

  companion object {

    private val TAG = DashboardFragment.javaClass.simpleName

    @JvmStatic
    fun newInstance() = DashboardFragment()
  }
}
