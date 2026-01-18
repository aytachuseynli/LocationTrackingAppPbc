package com.aytachuseynli.locationtrackerapp.data.local

import android.content.Context
import com.aytachuseynli.locationtrackerapp.domain.repository.TrackingController
import com.aytachuseynli.locationtrackerapp.service.LocationTrackingService
import com.aytachuseynli.locationtrackerapp.worker.LocationSyncWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrackingControllerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : TrackingController {

    override fun startTracking() {
        LocationTrackingService.startService(context)
    }

    override fun stopTracking() {
        LocationTrackingService.stopService(context)
    }

    override fun scheduleSync() {
        LocationSyncWorker.schedule(context)
    }

    override fun cancelSync() {
        LocationSyncWorker.cancel(context)
    }
}
