package com.aytachuseynli.locationtrackerapp.domain.model

data class LocationData(
    val id: Long = 0,
    val userId: String,
    val latitude: Double,
    val longitude: Double,
    val accuracy: Float,
    val timestamp: Long,
    val batteryLevel: Int,
    val isSynced: Boolean = false
)

data class User(
    val uid: String,
    val email: String,
    val displayName: String?
)

sealed class AuthState {
    data object Loading : AuthState()
    data object Unauthenticated : AuthState()
    data class Authenticated(val user: User) : AuthState()
    data class Error(val message: String) : AuthState()
}

sealed class SyncStatus {
    data object Idle : SyncStatus()
    data object Syncing : SyncStatus()
    data class Success(val syncedCount: Int) : SyncStatus()
    data class Error(val message: String) : SyncStatus()
}
