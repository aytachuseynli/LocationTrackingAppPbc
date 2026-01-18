package com.aytachuseynli.locationtrackerapp.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aytachuseynli.locationtrackerapp.domain.model.SyncStatus
import com.aytachuseynli.locationtrackerapp.domain.repository.AuthRepository
import com.aytachuseynli.locationtrackerapp.domain.repository.LocationRepository
import com.aytachuseynli.locationtrackerapp.domain.repository.TrackingController
import com.aytachuseynli.locationtrackerapp.domain.repository.TrackingPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val locationRepository: LocationRepository,
    private val trackingPreferences: TrackingPreferences,
    private val trackingController: TrackingController
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    private val currentUserId: String?
        get() = authRepository.currentUser?.uid

    init {
        loadUserInfo()
        loadTrackingState()
        observeLocationData()
    }

    // region Initialization
    private fun loadUserInfo() {
        authRepository.currentUser?.let { user ->
            updateState {
                copy(
                    userName = user.displayName ?: "User",
                    userEmail = user.email
                )
            }
        }
    }

    private fun loadTrackingState() {
        updateState { copy(isTrackingEnabled = trackingPreferences.isTrackingEnabled) }
    }

    private fun observeLocationData() {
        val userId = currentUserId ?: return

        viewModelScope.launch {
            combine(
                locationRepository.getRecentLocations(userId, 20),
                locationRepository.getUnsyncedCount()
            ) { locations, unsyncedCount ->
                locations to unsyncedCount
            }.collect { (locations, unsyncedCount) ->
                updateState {
                    copy(
                        recentLocations = locations,
                        unsyncedCount = unsyncedCount,
                        lastLocation = locations.firstOrNull()
                    )
                }
            }
        }
    }
    // endregion

    // region Public Actions
    fun toggleTracking() {
        val newState = !_uiState.value.isTrackingEnabled
        updateState { copy(isTrackingEnabled = newState) }
        saveTrackingState(newState)

        if (newState) startTracking() else stopTracking()
    }

    fun syncNow() {
        viewModelScope.launch {
            updateState { copy(syncStatus = SyncStatus.Syncing) }
            val result = locationRepository.syncLocations()
            updateState { copy(syncStatus = result) }

            delay(3000)
            updateState { copy(syncStatus = SyncStatus.Idle) }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            stopTracking()
            trackingPreferences.clear()
            authRepository.signOut()
        }
    }

    fun deleteAccount(onSuccess: () -> Unit) {
        viewModelScope.launch {
            updateState { copy(isDeleting = true, deleteError = null) }

            stopTracking()
            locationRepository.clearLocalData()
            trackingPreferences.clear()

            authRepository.deleteAccount().fold(
                onSuccess = {
                    updateState { copy(isDeleting = false) }
                    onSuccess()
                },
                onFailure = { error ->
                    updateState {
                        copy(
                            isDeleting = false,
                            deleteError = error.message ?: "Failed to delete account"
                        )
                    }
                }
            )
        }
    }

    fun clearDeleteError() {
        updateState { copy(deleteError = null) }
    }
    // endregion

    // region Private Helpers
    private fun saveTrackingState(enabled: Boolean) {
        trackingPreferences.isTrackingEnabled = enabled
        trackingPreferences.userId = currentUserId
    }

    private fun startTracking() {
        trackingController.startTracking()
        trackingController.scheduleSync()
    }

    private fun stopTracking() {
        trackingController.stopTracking()
        trackingController.cancelSync()
    }

    private inline fun updateState(transform: DashboardUiState.() -> DashboardUiState) {
        _uiState.update { it.transform() }
    }
    // endregion
}
