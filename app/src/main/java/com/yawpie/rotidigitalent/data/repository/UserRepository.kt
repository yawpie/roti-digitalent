package com.yawpie.rotidigitalent.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.util.Log
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.yawpie.rotidigitalent.data.User
import com.yawpie.rotidigitalent.data.database.UserDatabaseHelper
import com.yawpie.rotidigitalent.data.preferences.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val dbHelper: UserDatabaseHelper,
    private val sessionManager: SessionManager
) {
    suspend fun getUser(): Result<User> {
        return try {
            val userId = sessionManager.userIdFlow.first()
            val username = sessionManager.usernameFlow.first()
            val password = sessionManager.passwordFlow.first()
//            val token = sessionManager.tokenFlow.first()
            Result.success(
                User(userId!!, username!!, password!!)
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun registerUser(username: String, password: String): Result<Long> {
        return withContext(Dispatchers.IO) {
            try {
                val result = dbHelper.insertUser(username, password)
                if (result != -1L) {
                    Result.success(result)
                } else {
                    Result.failure(Exception("Gagal mendaftarkan pengguna"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun loginUser(username: String, password: String): Result<User> {
        return withContext(Dispatchers.IO) {
            try {
                val user = dbHelper.getUserByUsername(username)
                if (user != null && user.password == password) {
                    sessionManager.createLoginSession(user.id, user.username, user.password)
                    Result.success(user)
                } else {
                    Result.failure(Exception("Nama pengguna atau kata sandi salah"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun getCurrentLocation(context: Context, callback: (Float?, Float?) -> Unit) {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

        fusedLocationProviderClient.getCurrentLocation(
            LocationRequest.PRIORITY_HIGH_ACCURACY,
            null
        ).addOnSuccessListener { location: Location? ->
            try {
                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    Log.d("Location", "Latitude: $latitude, Longitude: $longitude")
                    callback(latitude.toFloat(), longitude.toFloat())
                } else {
                    throw Exception("Location is null")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                callback(null, null)
            }
        }.addOnFailureListener { exception ->
            Log.e("LocationError", "Error fetching location: ${exception.message}")
        }


    }

    suspend fun setLastKnownLocation(lat: Float, lon: Float): Result<Boolean> {
        return try {

            sessionManager.setLatestLocation(lat, lon)
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getLastKnownLocation(): Result<LatLng?> {
        return try {
            val location = sessionManager.locationFlow.first()
            Result.success(location)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout(): Result<Boolean> {
        return try {
            sessionManager.destroyUserData()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }

    }
}