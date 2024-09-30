package fr.infuseting.edt_app.util

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.rememberUpdatedState
import com.google.gson.JsonObject

object PreferencesManager {
    private const val SETTINGS_PREFS_NAME = "settings_prefs"
    private const val FAV_PREFS_NAME = "fav_prefs"
    private const val DARK_MODE_KEY = "dark_mode"
    private const val NOTIFICATION_DELAY_KEY = "notification_delay"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(SETTINGS_PREFS_NAME, Context.MODE_PRIVATE)
    }
    private fun getFavContext(context: Context): SharedPreferences {
        return context.getSharedPreferences(FAV_PREFS_NAME, Context.MODE_PRIVATE)
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
    fun getFav(context : Context, key : String) : String? {
        return getFavContext(context).getString(key, null)
    }
    fun addFav(context: Context, key: String, item : String) {
        getFavContext(context).edit().putString(key, item).apply()
    }
    fun removeFav(context: Context, key: String) {
        getFavContext(context).edit().remove(key).apply()
    }
    fun getFavList(context: Context) : List<String> {

        return getFavContext(context).all.keys.toList()
    }

}