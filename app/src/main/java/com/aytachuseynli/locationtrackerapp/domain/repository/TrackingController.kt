package com.aytachuseynli.locationtrackerapp.domain.repository

interface TrackingController {
    fun startTracking()
    fun stopTracking()
    fun scheduleSync()
    fun cancelSync()
}
