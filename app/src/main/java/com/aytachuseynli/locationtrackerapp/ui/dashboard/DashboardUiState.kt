package com.aytachuseynli.locationtrackerapp.ui.dashboard

import com.aytachuseynli.locationtrackerapp.domain.model.LocationData
import com.aytachuseynli.locationtrackerapp.domain.model.SyncStatus

data class DashboardUiState(
    val isTrackingEnabled: Boolean = false,
    val userName: String = "",
    val userEmail: String = "",
    val recentLocations: List<LocationData> = emptyList(),
    val unsyncedCount: Int = 0,
    val syncStatus: SyncStatus = SyncStatus.Idle,
    val lastLocation: LocationData? = null,
    val isDeleting: Boolean = false,
    val deleteError: String? = null
)
