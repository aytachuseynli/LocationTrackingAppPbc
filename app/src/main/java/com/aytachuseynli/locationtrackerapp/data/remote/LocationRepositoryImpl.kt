package com.aytachuseynli.locationtrackerapp.data.remote

import com.aytachuseynli.locationtrackerapp.data.local.dao.LocationDao
import com.aytachuseynli.locationtrackerapp.data.local.entity.LocationEntity
import com.aytachuseynli.locationtrackerapp.domain.model.LocationData
import com.aytachuseynli.locationtrackerapp.domain.model.SyncStatus
import com.aytachuseynli.locationtrackerapp.domain.repository.LocationRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationRepositoryImpl @Inject constructor(
    private val locationDao: LocationDao,
    private val firestore: FirebaseFirestore
) : LocationRepository {

    override suspend fun saveLocation(location: LocationData): Long {
        val entity = location.toEntity()
        return locationDao.insertLocation(entity)
    }

    override fun getLocationsByUser(userId: String): Flow<List<LocationData>> {
        return locationDao.getLocationsByUser(userId).map { entities ->
            entities.map { it.toLocationData() }
        }
    }

    override fun getRecentLocations(userId: String, limit: Int): Flow<List<LocationData>> {
        return locationDao.getRecentLocations(userId, limit).map { entities ->
            entities.map { it.toLocationData() }
        }
    }

    override suspend fun getLastLocation(userId: String): LocationData? {
        return locationDao.getLastLocation(userId)?.toLocationData()
    }

    override fun getUnsyncedCount(): Flow<Int> {
        return locationDao.getUnsyncedCount()
    }

    override suspend fun syncLocations(): SyncStatus {
        return try {
            val unsyncedLocations = locationDao.getUnsyncedLocations()

            if (unsyncedLocations.isEmpty()) {
                return SyncStatus.Success(0)
            }

            val batch = firestore.batch()
            val syncedIds = mutableListOf<Long>()

            unsyncedLocations.forEach { location ->
                val docRef = firestore.collection("locations").document()
                val data = hashMapOf(
                    "userId" to location.userId,
                    "latitude" to location.latitude,
                    "longitude" to location.longitude,
                    "accuracy" to location.accuracy,
                    "timestamp" to location.timestamp,
                    "batteryLevel" to location.batteryLevel,
                    "deviceLocalId" to location.id
                )
                batch.set(docRef, data)
                syncedIds.add(location.id)
            }

            batch.commit().await()
            locationDao.markMultipleAsSynced(syncedIds)

            SyncStatus.Success(syncedIds.size)
        } catch (e: Exception) {
            SyncStatus.Error(e.message ?: "Sync failed")
        }
    }

    override suspend fun cleanupOldLocations(olderThanDays: Int) {
        val cutoffTime = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(olderThanDays.toLong())
        locationDao.deleteOldSyncedLocations(cutoffTime)
    }

    override suspend fun clearLocalData() {
        locationDao.deleteAllLocations()
    }

    private fun LocationData.toEntity(): LocationEntity {
        return LocationEntity(
            id = id,
            userId = userId,
            latitude = latitude,
            longitude = longitude,
            accuracy = accuracy,
            timestamp = timestamp,
            batteryLevel = batteryLevel,
            isSynced = isSynced
        )
    }

    private fun LocationEntity.toLocationData(): LocationData {
        return LocationData(
            id = id,
            userId = userId,
            latitude = latitude,
            longitude = longitude,
            accuracy = accuracy,
            timestamp = timestamp,
            batteryLevel = batteryLevel,
            isSynced = isSynced
        )
    }
}
