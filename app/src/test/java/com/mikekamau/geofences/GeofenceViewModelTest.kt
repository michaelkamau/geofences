package com.mikekamau.geofences

import com.mikekamau.geofences.data.GeofenceRepository
import com.mikekamau.geofences.ui.GeofenceViewModel
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test


class GeofenceViewModelTest {

  private lateinit var geofenceViewModel: GeofenceViewModel

  private val repository = mockk<GeofenceRepository>(relaxed = true)

  @Before
  fun setUp() {
    geofenceViewModel = GeofenceViewModel(repository)
  }

  @Test
  fun `set valid geofence name`() {
    geofenceViewModel.setName("Public Playground")
    assertEquals(geofenceViewModel.getName().get(), "Public Playground")
    assertFalse(geofenceViewModel.errorInName.get())
  }

  @Test
  fun `set name with invalid value`() {
    geofenceViewModel.setName("")
    assertTrue(geofenceViewModel.errorInName.get())
  }

  @Test
  fun `set valid radius`(){
    geofenceViewModel.setRadius(100.0)
    assertEquals(geofenceViewModel.getRadius().get(), 100.0,0.0)
    assertFalse(geofenceViewModel.errorInRadius.get())
  }

  @Test
  fun `set invalid radius`(){
    geofenceViewModel.setRadius(0.0)
    assertTrue(geofenceViewModel.errorInRadius.get())
  }
}