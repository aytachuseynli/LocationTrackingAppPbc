package com.aytachuseynli.locationtrackerapp.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.aytachuseynli.locationtrackerapp.service.LocationTrackingService
import com.aytachuseynli.locationtrackerapp.util.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED ||
            intent.action == Intent.ACTION_MY_PACKAGE_REPLACED
        ) {
            val prefs = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE)
            val trackingEnabled = prefs.getBoolean(Constants.PREF_TRACKING_ENABLED, false)
            val userId = prefs.getString(Constants.PREF_USER_ID, null)

            if (trackingEnabled && !userId.isNullOrEmpty()) {
                LocationTrackingService.startService(context)
            }
        }
    }
}
