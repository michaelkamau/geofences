package com.mikekamau.geofences

import com.google.android.gms.location.Geofence
import com.mikekamau.geofences.data.GeofenceRepository
import com.mikekamau.geofences.ui.GeofenceViewModel
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test


class GeofenceViewModelTest {

  private lateinit var viewModel: GeofenceViewModel

  private val repository = mockk<GeofenceRepository>(relaxed = true)

  @Before
  fun setUp() {
    viewModel = GeofenceViewModel(repository)
  }

  @Test
  fun `set valid geofence name`() {
    viewModel.setName("Public Playground")
    assertEquals(viewModel.name.get(), "Public Playground")
    assertFalse(viewModel.errorInName.get())
  }

  @Test
  fun `set name with invalid value`() {
    viewModel.setName("")
    assertTrue(viewModel.errorInName.get())
  }

  @Test
  fun `set valid radius`() {
    viewModel.setRadius("100.0")
    assertEquals(viewModel.getRadius().get(), 100f, 0.0f)
    assertFalse(viewModel.errorInRadius.get())
  }

  @Test
  fun `set invalid radius`() {
    viewModel.setRadius("0.0")
    assertTrue(viewModel.errorInRadius.get())
  }

  @Test
  fun `set null to radius`() {
    viewModel.setRadius(null)
    assertTrue(viewModel.errorInRadius.get())
  }

  @Test
  fun `set all initial triggers`() {
    viewModel.initialEventDwell.set(true)
    viewModel.initialEventEnter.set(true)
    viewModel.initialEventExit.set(true)

    val initialTransitionTrigger = viewModel.getInitialTransitionTrigger()
    assertFalse(viewModel.errorInInitialEvent.get())
    assertEquals(
      initialTransitionTrigger,
      Geofence.GEOFENCE_TRANSITION_DWELL or
          Geofence.GEOFENCE_TRANSITION_EXIT or
          Geofence.GEOFENCE_TRANSITION_ENTER
    )
  }

  @Test
  fun `set initial enter and dwell triggers`() {
    viewModel.initialEventDwell.set(true)
    viewModel.initialEventEnter.set(true)

    val initialTransitionTrigger = viewModel.getInitialTransitionTrigger()
    assertFalse(viewModel.errorInInitialEvent.get())
    assertEquals(
      initialTransitionTrigger,
      Geofence.GEOFENCE_TRANSITION_DWELL or
          Geofence.GEOFENCE_TRANSITION_ENTER
    )
  }

  @Test
  fun `set initial enter and exit triggers`() {
    viewModel.initialEventExit.set(true)
    viewModel.initialEventEnter.set(true)

    val initialTransitionTrigger = viewModel.getInitialTransitionTrigger()
    assertFalse(viewModel.errorInInitialEvent.get())
    assertEquals(
      initialTransitionTrigger,
      Geofence.GEOFENCE_TRANSITION_EXIT or
          Geofence.GEOFENCE_TRANSITION_ENTER
    )
  }

  @Test
  fun `set initial dwell and exit triggers`() {
    viewModel.initialEventExit.set(true)
    viewModel.initialEventDwell.set(true)

    val initialTransitionTrigger = viewModel.getInitialTransitionTrigger()
    assertFalse(viewModel.errorInInitialEvent.get())
    assertEquals(
      initialTransitionTrigger,
      Geofence.GEOFENCE_TRANSITION_EXIT or
          Geofence.GEOFENCE_TRANSITION_DWELL
    )
  }

  @Test
  fun `set initial enter trigger`() {
    viewModel.initialEventEnter.set(true)

    val initialTransitionTrigger = viewModel.getInitialTransitionTrigger()
    assertFalse(viewModel.errorInInitialEvent.get())
    assertEquals(
      initialTransitionTrigger,
          Geofence.GEOFENCE_TRANSITION_ENTER
    )
  }

  @Test
  fun `set initial exit trigger`() {
    viewModel.initialEventExit.set(true)

    val initialTransitionTrigger = viewModel.getInitialTransitionTrigger()
    assertFalse(viewModel.errorInInitialEvent.get())
    assertEquals(
      initialTransitionTrigger,
      Geofence.GEOFENCE_TRANSITION_EXIT
    )
  }

  @Test
  fun `set initial dwell trigger`() {
    viewModel.initialEventDwell.set(true)

    val initialTransitionTrigger = viewModel.getInitialTransitionTrigger()
    assertFalse(viewModel.errorInInitialEvent.get())
    assertEquals(
      initialTransitionTrigger,
      Geofence.GEOFENCE_TRANSITION_DWELL
    )
  }


  @Test
  fun `error when no initial trigger set`() {
    viewModel.getInitialTransitionTrigger()
    assertTrue(viewModel.errorInInitialEvent.get())
  }
}