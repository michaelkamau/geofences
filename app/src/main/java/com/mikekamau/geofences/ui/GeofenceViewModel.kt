package com.mikekamau.geofences.ui

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableFloat
import androidx.lifecycle.*
import com.google.android.gms.location.Geofence
import com.google.android.gms.maps.model.LatLng
import com.mikekamau.geofences.data.GeofenceRepository
import com.mikekamau.geofences.data.GeofenceUtils
import com.mikekamau.geofences.models.GeofenceModel
import kotlinx.coroutines.launch

class GeofenceViewModel(
  private val repository: GeofenceRepository
) : ViewModel() {

  //==============
  // Geofence Name
  //==============
  val name: ObservableField<String> = ObservableField()
  val errorInName: ObservableBoolean = ObservableBoolean()

  //================
  // Geofence Radius
  //================
  private val radius: ObservableFloat = ObservableFloat()
  val errorInRadius = ObservableBoolean()

  //=========================
  // Geofence Initial Events
  //=========================
  val initialEventEnter = ObservableBoolean()
  val initialEventExit = ObservableBoolean()
  val initialEventDwell = ObservableBoolean()
  val errorInInitialEvent = ObservableBoolean(false)

  //================
  // Geofence Events
  //================
  val eventEnter = ObservableBoolean()
  val eventExit = ObservableBoolean()
  val eventDwell = ObservableBoolean()
  val errorInEvent = ObservableBoolean()
  var initialTransitionTrigger: Int? = null

  //================
  // Geofence Fields
  //================
  var selectedPosition: LatLng? = null


  fun setName(geofenceName: String?) = when {
    geofenceName.isNullOrBlank() -> {
      errorInName.set(true)
    }
    else -> {
      name.set(geofenceName)
      errorInName.set(false)
    }
  }

  fun setRadius(value: String?) {
    if (value.isNullOrBlank()) {
      errorInRadius.set(true)
      return
    } else {
      try {
        val rad = value.toFloat()
        when {
          rad <= 0.0 -> {
            errorInRadius.set(true)
          }
          else -> {
            errorInRadius.set(false)
            radius.set(rad)
          }
        }
      } catch (e: NumberFormatException) {
        errorInRadius.set(true)
        return
      }
    }
  }

  fun getRadius() = radius

  fun insert(geoFence: GeofenceModel) = viewModelScope.launch {
    repository.insert(geoFence)
  }

  fun getInitialTransitionTrigger() = when {
    initialEventEnter.get() && initialEventDwell.get() && initialEventExit.get() -> {
      Geofence.GEOFENCE_TRANSITION_ENTER or
          Geofence.GEOFENCE_TRANSITION_DWELL or
          Geofence.GEOFENCE_TRANSITION_EXIT
    }
    initialEventEnter.get() && initialEventDwell.get() -> {
      Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_DWELL
    }
    initialEventEnter.get() && initialEventExit.get() -> {
      Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT
    }
    initialEventDwell.get() && initialEventExit.get() -> {
      Geofence.GEOFENCE_TRANSITION_DWELL or Geofence.GEOFENCE_TRANSITION_EXIT
    }
    initialEventEnter.get() -> {
      Geofence.GEOFENCE_TRANSITION_ENTER
    }
    initialEventDwell.get() -> {
      Geofence.GEOFENCE_TRANSITION_DWELL
    }
    initialEventExit.get() -> {
      Geofence.GEOFENCE_TRANSITION_EXIT
    }
    else -> {
      errorInInitialEvent.set(true)
      GeofenceUtils.DEFAULT_TRIGGER
    }
  }

  fun setInitialTransition(trigger: Int) {
    initialTransitionTrigger = trigger
  }

  fun hasFieldsErrors(): Boolean {
    // todo: update this
    return errorInName.get() || errorInInitialEvent.get()
  }

  val allGeoFences: LiveData<List<GeofenceModel>> = repository.allGeofences.asLiveData()
}

class GeofenceViewModelFactory(
  private val repository: GeofenceRepository
) : ViewModelProvider.Factory {

  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    if (modelClass.isAssignableFrom(GeofenceViewModel::class.java)) {
      @Suppress("UNCHECKED_CAST")
      return GeofenceViewModel(repository) as T
    }
    throw IllegalArgumentException("Cannot create GeofenceViewModel instance")
  }
}