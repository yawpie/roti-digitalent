package com.fintrack.rotidigitalent.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore by preferencesDataStore(name = "session")

@Singleton
class SessionManager @Inject constructor(@ApplicationContext private val context: Context) {
    companion object {
        val USER_ID_KEY = intPreferencesKey("id")
        val NAME_KEY = stringPreferencesKey("username")
        val LATITUDE_KEY = floatPreferencesKey("latitude")
        val LONGITUDE_KEY = floatPreferencesKey("longitude")
        val PASSWORD_KEY = stringPreferencesKey("password")
    }

    private val userDataFlow = context.dataStore

    val userIdFlow: Flow<Int?> = context.dataStore.data.map { preferences ->
        preferences[USER_ID_KEY]
    }

    val usernameFlow: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[NAME_KEY]
    }

    val passwordFlow: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[PASSWORD_KEY]
    }

//    val tokenFlow: Flow<String?> = context.dataStore.data.map { preferences ->
//        preferences[TOKEN_KEY]
//    }

    val locationFlow: Flow<LatLng?> = context.dataStore.data.map { preferences ->
        if (preferences[LATITUDE_KEY] != null || preferences[LONGITUDE_KEY] != null) {
            val latitude = preferences[LATITUDE_KEY]!!.toDouble()
            val longitude = preferences[LONGITUDE_KEY]!!.toDouble()
            val location = LatLng(latitude, longitude)
            location
        } else {
            null
        }
    }

    fun saveUserData(userId: Int, name: String, token: String) {
        CoroutineScope(Dispatchers.IO).launch {
            userDataFlow.edit { preferences ->
                preferences[USER_ID_KEY] = userId
                preferences[NAME_KEY] = name
//                preferences[TOKEN_KEY] = token
            }
        }
    }

    suspend fun createLoginSession(userId: Int, username: String, password: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = userId
            preferences[NAME_KEY] = username
            preferences[PASSWORD_KEY] = password
        }
    }

    fun destroyUserData() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                userDataFlow.edit { preferences ->
                    preferences.clear()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

//    suspend fun getTokenOnce(): String? {
//        return context.dataStore.data
//            .map { preferences -> preferences[TOKEN_KEY] }
//            .first()
//    }

    fun deleteUserData() {
        CoroutineScope(Dispatchers.IO).launch {
            userDataFlow.edit { preferences ->
                preferences.remove(USER_ID_KEY)
                preferences.remove(NAME_KEY)
//                preferences.remove(TOKEN_KEY)
            }
        }
    }
    suspend fun setLatestLocation(latitude: Float, longitude: Float) {
        context.dataStore.edit { preferences ->
            preferences[LATITUDE_KEY] = latitude
            preferences[LONGITUDE_KEY] = longitude
        }
    }

    /**
     * Menghapus semua data sesi pengguna (logout).
     */
    suspend fun clearSession() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}