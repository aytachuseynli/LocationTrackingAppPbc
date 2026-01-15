package com.aytachuseynli.locationtrackerapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aytachuseynli.locationtrackerapp.data.local.entity.LocationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocation(location: LocationEntity): Long

    @Query("SELECT * FROM locations WHERE userId = :userId ORDER BY timestamp DESC")
    fun getLocationsByUser(userId: String): Flow<List<LocationEntity>>

    @Query("SELECT * FROM locations WHERE isSynced = 0 ORDER BY timestamp ASC")
    suspend fun getUnsyncedLocations(): List<LocationEntity>

    @Query("SELECT * FROM locations WHERE isSynced = 0")
    fun getUnsyncedLocationsFlow(): Flow<List<LocationEntity>>

    @Query("UPDATE locations SET isSynced = 1 WHERE id = :locationId")
    suspend fun markAsSynced(locationId: Long)

    @Query("UPDATE locations SET isSynced = 1 WHERE id IN (:locationIds)")
    suspend fun markMultipleAsSynced(locationIds: List<Long>)

    @Query("SELECT COUNT(*) FROM locations WHERE isSynced = 0")
    fun getUnsyncedCount(): Flow<Int>

    @Query("SELECT * FROM locations WHERE userId = :userId ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLastLocation(userId: String): LocationEntity?

    @Query("SELECT * FROM locations WHERE userId = :userId ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentLocations(userId: String, limit: Int): Flow<List<LocationEntity>>

    @Query("DELETE FROM locations WHERE isSynced = 1 AND timestamp < :olderThan")
    suspend fun deleteOldSyncedLocations(olderThan: Long)

    @Query("DELETE FROM locations WHERE userId = :userId")
    suspend fun deleteAllLocationsForUser(userId: String)

    @Query("DELETE FROM locations")
    suspend fun deleteAllLocations()
}
