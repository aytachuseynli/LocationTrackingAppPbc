package com.aytachuseynli.locationtrackerapp.util

object Constants {
    const val LOCATION_UPDATE_INTERVAL = 5 * 60 * 1000L // 5 minutes
    const val LOCATION_FASTEST_INTERVAL = 2 * 60 * 1000L // 2 minutes minimum
    const val LOCATION_MAX_WAIT_TIME = 10 * 60 * 1000L // 10 minutes max

    const val NOTIFICATION_CHANNEL_ID = "location_tracking_channel"
    const val NOTIFICATION_CHANNEL_NAME = "Location Tracking"
    const val NOTIFICATION_ID = 1001

    const val SYNC_WORK_NAME = "location_sync_work"
    const val SYNC_WORK_TAG = "sync_tag"

    const val PREFS_NAME = "location_tracker_prefs"
    const val PREF_TRACKING_ENABLED = "tracking_enabled"
    const val PREF_USER_ID = "user_id"
}
