package com.mikekamau.geofences.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.mikekamau.geofences.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

  val geofencesViewModel: GeofenceViewModel by activityViewModels()
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
  }

  companion object {

    @JvmStatic
    fun newInstance() = DashboardFragment()
  }
}
