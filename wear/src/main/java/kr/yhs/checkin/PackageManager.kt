package kr.yhs.checkin

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit


class PackageManager(private val preferencesName: String, private val context: Context) {

    private fun getPreferences(): SharedPreferences {
        return context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE)
    }

    fun setString(key: String, value: String) {
        val prefs = getPreferences()
        prefs.apply {
            edit {
                putString(key, value)
                commit()
            }
        }
    }

    fun setBoolean(key: String, value: Boolean) {
        val prefs = getPreferences()
        prefs.apply {
            edit {
                putBoolean(key, value)
                commit()
            }
        }
    }

    fun setInt(key: String, value: Int) {
        val prefs = getPreferences()
        prefs.apply {
            edit {
                putInt(key, value)
                commit()
            }
        }
    }

    fun getString(key: String): String? {
        val prefs = getPreferences()
        return prefs.getString(key, "")
    }

    fun getBoolean(key: String): Boolean {
        val prefs = getPreferences()
        return prefs.getBoolean(key, false)
    }

    fun getInt(key: String): Int {
        val prefs = getPreferences()
        return prefs.getInt(key, 0)
    }

    fun removeKey(key: String) {
        val prefs = getPreferences()
        prefs.apply {
            edit {
                remove(key)
                commit()
            }
        }
    }

    fun clear(context: Context) {
        val prefs = getPreferences()
        prefs.apply {
            edit {
                clear()
                commit()
            }
        }
    }
}