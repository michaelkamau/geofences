package com.mikekamau.geofences.ui

import androidx.lifecycle.*
import com.mikekamau.geofences.data.GeofenceRepository
import com.mikekamau.geofences.models.Geofence
import kotlinx.coroutines.launch

class GeofenceViewModel(
  private val repository: GeofenceRepository
) : ViewModel() {

  fun insert(geoFence: Geofence) = viewModelScope.launch {
    repository.insert(geoFence)
  }

  val allGeoFences: LiveData<List<Geofence>> = repository.allGeofences.asLiveData()
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