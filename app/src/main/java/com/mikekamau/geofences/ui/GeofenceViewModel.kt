package com.mikekamau.geofences.ui

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableDouble
import androidx.databinding.ObservableField
import androidx.lifecycle.*
import com.mikekamau.geofences.data.GeofenceRepository
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
  private val radius: ObservableDouble = ObservableDouble()
  val errorInRadius = ObservableBoolean()

  //=========================
  // Geofence Initial Events
  //=========================
  val initialEventEnter = ObservableBoolean()
  val initialEventExit = ObservableBoolean()
  val initialEventDwell = ObservableBoolean()
  val errorInInitialEvent = ObservableBoolean()

  //================
  // Geofence Events
  //================
  val eventEnter = ObservableBoolean()
  val eventExit = ObservableBoolean()
  val eventDwell = ObservableBoolean()
  val errorInEvent = ObservableBoolean()

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
        val rad = value.toDouble()
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