package com.mikekamau.geofences.ui

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.*
import com.mikekamau.geofences.data.GeofenceRepository
import com.mikekamau.geofences.models.GeofenceModel
import kotlinx.coroutines.launch

class GeofenceViewModel(
  private val repository: GeofenceRepository
) : ViewModel() {

  private val name: ObservableField<String> = ObservableField()
  public val errorInName: ObservableBoolean = ObservableBoolean()

  fun setName(geofenceName: String) = when {
    geofenceName.isEmpty() -> {
      errorInName.set(true)
    }
    else -> {
      name.set(geofenceName)
      errorInName.set(false)
    }
  }

  fun getName() = name

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