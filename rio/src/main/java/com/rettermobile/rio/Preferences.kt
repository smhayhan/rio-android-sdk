package com.rettermobile.rio

import android.content.SharedPreferences
import androidx.preference.PreferenceManager

object Preferences {

    object Keys {
        const val TOKEN_INFO = "token_info"
        const val TOKEN_INFO_DELTA = "token_info_delta"
    }

    // default mode private
    var pref: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(RioConfig.applicationContext)

    fun getString(key: String, defValue: String): String? {
        return pref.getString(key, defValue)
    }

    fun getString(key: String): String? {
        return pref.getString(key, "")
    }

    fun deleteKey(key: String) {
        val editor = pref.edit()
        editor.remove(key)
        editor.apply()
    }

    fun setString(key: String, newValue: String) {
        val editor = pref.edit()
        editor.putString(key, newValue)
        editor.commit()
    }

    fun setInt(key: String, newValue: Int) {
        val editor = pref.edit()
        editor.putInt(key, newValue)
        editor.commit()
    }

    fun setLong(key: String, newValue: Long) {
        val editor = pref.edit()
        editor.putLong(key, newValue)
        editor.commit()
    }

    fun setBoolean(key: String, newValue: Boolean?) {
        val editor = pref.edit()
        editor.putBoolean(key, newValue!!)
        editor.commit()
    }

    fun getInt(key: String, defValue: Int): Int {
        return pref.getInt(key, defValue)
    }

    fun getFloat(key: String, defValue: Float): Float {
        return pref.getFloat(key, defValue)
    }

    fun getLong(key: String, defValue: Long): Long {
        return pref.getLong(key, defValue)
    }

    fun getBoolean(key: String, defValue: Boolean): Boolean {
        return pref.getBoolean(key, defValue)
    }

    fun clearAllData() {
        val editor = pref.edit()
        editor.clear()
        editor.commit()
    }
}