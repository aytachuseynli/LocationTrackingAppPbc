package com.aytachuseynli.locationtrackerapp.domain.repository

interface TrackingPreferences {
    var isTrackingEnabled: Boolean
    var userId: String?
    fun clear()
}
