package fr.infuseting.edt_app.util

import android.content.Context
import android.content.SharedPreferences

object PreferencesManager {
    private const val PREFS_NAME = "settings_prefs"
    private const val DARK_MODE_KEY = "dark_mode"
    private const val NOTIFICATION_DELAY_KEY = "notification_delay"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun setDarkModeEnabled(context: Context, isEnabled: Boolean) {
        getPreferences(context).edit().putBoolean(DARK_MODE_KEY, isEnabled).apply()
    }

    fun isDarkModeEnabled(context: Context): Boolean? {
        return if (getPreferences(context).contains(DARK_MODE_KEY)) {
            getPreferences(context).getBoolean(DARK_MODE_KEY, false)
        } else {
            null
        }
    }

    fun setNotificationDelay(context: Context, delay: Int) {
        getPreferences(context).edit().putInt(NOTIFICATION_DELAY_KEY, delay).apply()
    }

    fun getNotificationDelay(context: Context): Int {
        return getPreferences(context).getInt(NOTIFICATION_DELAY_KEY, 1)
    }
}