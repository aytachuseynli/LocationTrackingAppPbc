package com.aytachuseynli.locationtrackerapp.di

import android.content.Context
import androidx.room.Room
import com.aytachuseynli.locationtrackerapp.data.local.TrackingControllerImpl
import com.aytachuseynli.locationtrackerapp.data.local.TrackingPreferencesImpl
import com.aytachuseynli.locationtrackerapp.data.local.dao.LocationDao
import com.aytachuseynli.locationtrackerapp.data.local.database.LocationDatabase
import com.aytachuseynli.locationtrackerapp.data.remote.FirebaseAuthRepository
import com.aytachuseynli.locationtrackerapp.data.remote.LocationRepositoryImpl
import com.aytachuseynli.locationtrackerapp.domain.repository.AuthRepository
import com.aytachuseynli.locationtrackerapp.domain.repository.LocationRepository
import com.aytachuseynli.locationtrackerapp.domain.repository.TrackingController
import com.aytachuseynli.locationtrackerapp.domain.repository.TrackingPreferences
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // Firebase
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    // Room Database
    @Provides
    @Singleton
    fun provideLocationDatabase(
        @ApplicationContext context: Context
    ): LocationDatabase {
        return Room.databaseBuilder(
            context,
            LocationDatabase::class.java,
            LocationDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideLocationDao(database: LocationDatabase): LocationDao {
        return database.locationDao()
    }

    // Location
    @Provides
    @Singleton
    fun provideFusedLocationClient(
        @ApplicationContext context: Context
    ): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(context)
    }

    // Repositories
    @Provides
    @Singleton
    fun provideAuthRepository(
        firebaseAuth: FirebaseAuth,
        firestore: FirebaseFirestore
    ): AuthRepository {
        return FirebaseAuthRepository(firebaseAuth, firestore)
    }

    @Provides
    @Singleton
    fun provideLocationRepository(
        locationDao: LocationDao,
        firestore: FirebaseFirestore
    ): LocationRepository {
        return LocationRepositoryImpl(locationDao, firestore)
    }

    // Tracking
    @Provides
    @Singleton
    fun provideTrackingPreferences(
        impl: TrackingPreferencesImpl
    ): TrackingPreferences = impl

    @Provides
    @Singleton
    fun provideTrackingController(
        impl: TrackingControllerImpl
    ): TrackingController = impl
}
