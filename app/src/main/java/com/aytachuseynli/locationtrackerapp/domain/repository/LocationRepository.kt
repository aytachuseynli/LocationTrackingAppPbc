package com.aytachuseynli.locationtrackerapp.domain.repository

import com.aytachuseynli.locationtrackerapp.domain.model.LocationData
import com.aytachuseynli.locationtrackerapp.domain.model.SyncStatus
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    suspend fun saveLocation(location: LocationData): Long
    fun getLocationsByUser(userId: String): Flow<List<LocationData>>
    fun getRecentLocations(userId: String, limit: Int): Flow<List<LocationData>>
    suspend fun getLastLocation(userId: String): LocationData?
    fun getUnsyncedCount(): Flow<Int>
    suspend fun syncLocations(): SyncStatus
    suspend fun cleanupOldLocations(olderThanDays: Int = 7)
    suspend fun clearLocalData()
}
