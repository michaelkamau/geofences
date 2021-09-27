package com.mikekamau.geofences.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.mikekamau.geofences.GeofencesApplication
import com.mikekamau.geofences.R
import com.mikekamau.geofences.databinding.FragmentAddGeofenceBinding


class AddGeofenceFragment : DialogFragment() {

  private val viewModel: GeofenceViewModel by activityViewModels {
    GeofenceViewModelFactory(GeofencesApplication.getInstance().repository)
  }

  lateinit var binding: FragmentAddGeofenceBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    binding = FragmentAddGeofenceBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding.btnSave.setOnClickListener {
      saveGeofence()
    }
  }

  private fun saveGeofence() {
    viewModel.setName(binding.etName.text.toString())
    showNameError(viewModel.errorInName.get())
    viewModel.setRadius(binding.etRadius.text.toString())
    showRadiusError(viewModel.errorInRadius.get())


  }

  private fun showRadiusError(hasError: Boolean) {
    if (hasError) {
      binding.textLayoutRadius.error = getString(R.string.error_enter_valid_radius)
    } else {
      binding.textLayoutRadius.error = null
    }
  }

  private fun showNameError(hasError: Boolean) {
    if (hasError) {
      binding.textLayoutName.error = getString(R.string.error_enter_valid_name)
    } else {
      binding.textLayoutName.error = null
    }
  }

  companion object {

    const val TAG = "AddGeofenceFragment"

    @JvmStatic
    fun newInstance() = AddGeofenceFragment()
  }
}