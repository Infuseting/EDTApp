package fr.infuseting.edt_app.util

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.rememberUpdatedState
import com.google.gson.JsonObject

object PreferencesManager {
    private const val SETTINGS_PREFS_NAME = "settings_prefs"
    private const val FAV_PREFS_NAME = "fav_prefs"
    private const val UPDATE_PREFS_NAME = "update_prefs"
    private const val LAST_UPDATE_PREFS_NAME = "last_update_prefs"
    private const val DARK_MODE_KEY = "dark_mode"
    private const val START_TIME_NOTIFICATION = "start_time_notification"
    private const val END_TIME_NOTIFICATION = "end_time_notification"
    private const val REQUEST_PER_MINUTE = "auto_request_per_minute"
    private const val NOTIFICATION_DELAY_KEY = "notification_delay"
    private const val LAST_UPDATE_COURS = "last_update_cours"
    private const val STRING_COURS = "string_cours"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(SETTINGS_PREFS_NAME, Context.MODE_PRIVATE)
    }
    private fun getFavContext(context: Context): SharedPreferences {
        return context.getSharedPreferences(FAV_PREFS_NAME, Context.MODE_PRIVATE)
    }
    private fun getUpdateContext(context: Context): SharedPreferences {
        return context.getSharedPreferences(UPDATE_PREFS_NAME, Context.MODE_PRIVATE)
    }
    private fun getLastUpdateContext(context: Context): SharedPreferences {
        return context.getSharedPreferences(LAST_UPDATE_PREFS_NAME, Context.MODE_PRIVATE)
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
    fun setRequestPerMinute(context: Context, value: Int) {
        getPreferences(context).edit().putInt(REQUEST_PER_MINUTE, value).apply()
    }
    fun getRequestPerMinute(context: Context): Int {
        return getPreferences(context).getInt(REQUEST_PER_MINUTE, 15)
    }
    fun setStartTime(context: Context, time: Int) {
        getPreferences(context).edit().putInt(START_TIME_NOTIFICATION, time).apply()
    }
    fun getStartTime(context: Context): Int {
        return getPreferences(context).getInt(START_TIME_NOTIFICATION, 7)
    }
    fun setEndTime(context: Context, time: Int) {
        getPreferences(context).edit().putInt(END_TIME_NOTIFICATION, time).apply()
    }
    fun getEndTime(context: Context): Int {
        return getPreferences(context).getInt(END_TIME_NOTIFICATION, 23)
    }


    fun setNotificationDelay(context: Context, delay: Int) {
        getPreferences(context).edit().putInt(NOTIFICATION_DELAY_KEY, delay).apply()
    }

    fun getNotificationDelay(context: Context): Int {
        return getPreferences(context).getInt(NOTIFICATION_DELAY_KEY, 7)
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


    fun getLastUpdate(context : Context, key : String) : Long? {
        return getLastUpdateContext(context).getLong(key, 0)
    }
    fun setLastUpdate(context: Context, key: String, item : Long) {
        getLastUpdateContext(context).edit().putLong(key, item).apply()
    }
    fun removeLastUpdate(context: Context, key: String) {
        getLastUpdateContext(context).edit().remove(key).apply()
    }
    fun getLastUpdateList(context: Context) : List<String> {

        return getLastUpdateContext(context).all.keys.toList()
    }
    fun getStringCours(context : Context, key : String) : String? {
        return getUpdateContext(context).getString(key, null)
    }
    fun setStringCours(context: Context, key: String, item : String) {
        getUpdateContext(context).edit().putString(key, item).apply()
    }
    fun removeStringCours(context: Context, key: String) {
        getUpdateContext(context).edit().remove(key).apply()
    }
    fun getStringCoursList(context: Context) : List<String> {

        return getUpdateContext(context).all.keys.toList()
    }


}