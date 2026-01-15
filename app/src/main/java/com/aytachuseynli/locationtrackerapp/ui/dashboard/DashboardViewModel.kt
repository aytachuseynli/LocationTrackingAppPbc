package com.aytachuseynli.locationtrackerapp.ui.dashboard

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aytachuseynli.locationtrackerapp.domain.model.SyncStatus
import com.aytachuseynli.locationtrackerapp.domain.repository.AuthRepository
import com.aytachuseynli.locationtrackerapp.domain.repository.LocationRepository
import com.aytachuseynli.locationtrackerapp.service.LocationTrackingService
import com.aytachuseynli.locationtrackerapp.util.Constants
import com.aytachuseynli.locationtrackerapp.worker.LocationSyncWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val authRepository: AuthRepository,
    private val locationRepository: LocationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    private val prefs = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE)

    init {
        loadUserInfo()
        observeLocationData()
        loadTrackingState()
    }

    private fun loadUserInfo() {
        authRepository.currentUser?.let { user ->
            _uiState.value = _uiState.value.copy(
                userName = user.displayName ?: "User",
                userEmail = user.email
            )
        }
    }

    private fun loadTrackingState() {
        val isTracking = prefs.getBoolean(Constants.PREF_TRACKING_ENABLED, false)
        _uiState.value = _uiState.value.copy(isTrackingEnabled = isTracking)
    }

    private fun observeLocationData() {
        val userId = authRepository.currentUser?.uid ?: return

        viewModelScope.launch {
            combine(
                locationRepository.getRecentLocations(userId, 20),
                locationRepository.getUnsyncedCount()
            ) { locations, unsyncedCount ->
                Pair(locations, unsyncedCount)
            }.collect { (locations, unsyncedCount) ->
                _uiState.value = _uiState.value.copy(
                    recentLocations = locations,
                    unsyncedCount = unsyncedCount,
                    lastLocation = locations.firstOrNull()
                )
            }
        }
    }

    fun toggleTracking() {
        val newState = !_uiState.value.isTrackingEnabled
        _uiState.value = _uiState.value.copy(isTrackingEnabled = newState)

        // Save state
        prefs.edit()
            .putBoolean(Constants.PREF_TRACKING_ENABLED, newState)
            .putString(Constants.PREF_USER_ID, authRepository.currentUser?.uid)
            .apply()

        if (newState) {
            startTracking()
        } else {
            stopTracking()
        }
    }

    private fun startTracking() {
        LocationTrackingService.startService(context)
        LocationSyncWorker.schedule(context)
    }

    private fun stopTracking() {
        LocationTrackingService.stopService(context)
        LocationSyncWorker.cancel(context)
    }

    fun syncNow() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(syncStatus = SyncStatus.Syncing)

            val result = locationRepository.syncLocations()
            _uiState.value = _uiState.value.copy(syncStatus = result)

            // Reset status after delay
            kotlinx.coroutines.delay(3000)
            _uiState.value = _uiState.value.copy(syncStatus = SyncStatus.Idle)
        }
    }

    fun signOut() {
        viewModelScope.launch {
            stopTracking()
            prefs.edit().clear().apply()
            authRepository.signOut()
        }
    }

    fun deleteAccount(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isDeleting = true, deleteError = null)

            // Stop tracking first
            stopTracking()

            // Clear local data
            locationRepository.clearLocalData()
            prefs.edit().clear().apply()

            // Delete account from Firebase
            val result = authRepository.deleteAccount()

            result.fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(isDeleting = false)
                    onSuccess()
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isDeleting = false,
                        deleteError = error.message ?: "Failed to delete account"
                    )
                }
            )
        }
    }

    fun clearDeleteError() {
        _uiState.value = _uiState.value.copy(deleteError = null)
    }
}
