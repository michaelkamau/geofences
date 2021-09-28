package com.mikekamau.geofences.db

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mikekamau.geofences.data.db.GeofenceDao
import com.mikekamau.geofences.data.db.GeofenceDatabase
import com.mikekamau.geofences.models.GeoPoint
import com.mikekamau.geofences.models.GeofenceModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class GeofenceModelDaoTest {

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
  fun getZeroGeofence() = runBlocking {
    val res = geofenceDao.getAll().first()
    assertTrue("result should be empty list", res.isEmpty())
  }

  @Test
  fun insertAndGetSingleGeofence() = runBlocking {
    val geo = GeofenceModel(
      1,
      GeoPoint(1.2921, 36.8219),
      1631957150427,
      1631957150427,
      "enter",
      "location1",
      "Shopping Mall"
    )

    geofenceDao.insertAll(geo)
    val geoRes = geofenceDao.getAll().first()
    assertEquals("result should be equal to $geo ", geoRes[0], geo)
  }

  @Test
  fun insertAndGetMultipleGeofences() = runBlocking {
    val geo1 = GeofenceModel(
      1,
      GeoPoint(1.2921, 36.8219),
      1631957150427,
      1631957150427,
      "enter",
      "location1",
      "Shopping Mall"
    )

    val geo2 = GeofenceModel(
      2,
      GeoPoint(1.2921, 36.8219),
      1631957150427,
      1631957150427,
      "exit",
      "location1",
      "Shopping Mall"
    )

    geofenceDao.insertAll(geo1, geo2)
    val geoRes = geofenceDao.getAll().first()
    assertEquals(geoRes[0], geo1)
    assertEquals(geoRes[1], geo2)
  }

  @Test
  fun insertAndDeleteGeofence() = runBlocking {
    val geo = GeofenceModel(
      1,
      GeoPoint(1.2921, 36.8219),
      1631957150427,
      1631957150427,
      "enter",
      "location1",
      "Shopping Mall"
    )

    geofenceDao.insertAll(geo)
    val rowsDeleted = geofenceDao.delete(geo)

    assertEquals("rows deleted should be 1", rowsDeleted, 1)

    val res = geofenceDao.getAll().first()
    assertTrue("database should be empty", res.isEmpty())
  }
}