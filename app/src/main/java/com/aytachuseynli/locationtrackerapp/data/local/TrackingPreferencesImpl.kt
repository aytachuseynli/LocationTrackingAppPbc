package com.aytachuseynli.locationtrackerapp.data.local

import android.content.Context
import android.content.SharedPreferences
import com.aytachuseynli.locationtrackerapp.domain.repository.TrackingPreferences
import com.aytachuseynli.locationtrackerapp.util.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrackingPreferencesImpl @Inject constructor(
    @ApplicationContext context: Context
) : TrackingPreferences {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE)

    override var isTrackingEnabled: Boolean
        get() = prefs.getBoolean(Constants.PREF_TRACKING_ENABLED, false)
        set(value) = prefs.edit().putBoolean(Constants.PREF_TRACKING_ENABLED, value).apply()

    override var userId: String?
        get() = prefs.getString(Constants.PREF_USER_ID, null)
        set(value) = prefs.edit().putString(Constants.PREF_USER_ID, value).apply()

    override fun clear() = prefs.edit().clear().apply()
}
