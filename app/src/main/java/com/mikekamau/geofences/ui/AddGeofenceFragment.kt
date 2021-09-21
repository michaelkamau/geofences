package com.mikekamau.geofences.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.mikekamau.geofences.databinding.FragmentAddGeofenceBinding


class AddGeofenceFragment : DialogFragment() {

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

  companion object {

    const val TAG = "AddGeofenceFragment"

    @JvmStatic
    fun newInstance() = AddGeofenceFragment()
  }
}