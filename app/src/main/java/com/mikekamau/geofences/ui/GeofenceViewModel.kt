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

  private val name: ObservableField<String> = ObservableField()
  val errorInName: ObservableBoolean = ObservableBoolean()
  private val radius: ObservableDouble = ObservableDouble()
  val errorInRadius = ObservableBoolean()

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

  fun setRadius(value: Double) = when {
    //TODO: set proper min radius
    value <= 0.0 -> {
      errorInRadius.set(true)
    }
    else -> {
      radius.set(value)
      errorInRadius.set(false)
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