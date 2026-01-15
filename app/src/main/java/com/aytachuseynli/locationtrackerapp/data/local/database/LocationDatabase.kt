package com.aytachuseynli.locationtrackerapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.aytachuseynli.locationtrackerapp.data.local.dao.LocationDao
import com.aytachuseynli.locationtrackerapp.data.local.entity.LocationEntity

@Database(
    entities = [LocationEntity::class],
    version = 1,
    exportSchema = false
)
abstract class LocationDatabase : RoomDatabase() {
    abstract fun locationDao(): LocationDao

    companion object {
        const val DATABASE_NAME = "location_tracker_db"
    }
}
