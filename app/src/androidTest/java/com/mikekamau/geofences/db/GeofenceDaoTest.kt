package com.mikekamau.geofences.db

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mikekamau.geofences.data.db.GeofenceDao
import com.mikekamau.geofences.data.db.GeofenceDatabase
import com.mikekamau.geofences.models.GeoPoint
import com.mikekamau.geofences.models.Geofence
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class GeofenceDaoTest {
  private lateinit var geofenceDao: GeofenceDao
  private lateinit var db: GeofenceDatabase

  @Before
  fun createDb() {
    val context: Context = ApplicationProvider.getApplicationContext()
    db = Room.inMemoryDatabaseBuilder(context, GeofenceDatabase::class.java)
      .allowMainThreadQueries()
      .build()
    geofenceDao = db.geofenceDao()
  }

  @After
  @Throws(IOException::class)
  fun closeDb() {
    db.close()
  }

  @Test
  fun insertAndGetSingleGeofence() = runBlocking {
    val geo = Geofence(
      1,
      GeoPoint(1.2921, 36.8219),
      1631957150427,
      1631957150427,
      "enter",
      "location1",
      "Shopping Mall"
    )

    geofenceDao.insertAll(geo)
    val geoRes =  geofenceDao.getAll().first()
    assertEquals(geoRes[0], geo)
  }
}