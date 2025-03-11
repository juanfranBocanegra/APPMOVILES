package com.app.dolt.utils

import android.content.Context
import android.content.SharedPreferences

object SharedPreferencesManager {

    private const val PREFS_NAME = "user_preferences"
    private const val TOKEN_KEY = "BEARER_TOKEN"

    // Obtener las SharedPreferences
    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    // Guardar el token
    fun saveToken(context: Context, token: String) {
        val editor = getPreferences(context).edit()
        editor.putString(TOKEN_KEY, token)
        editor.apply()
    }

    // Obtener el token
    fun getToken(context: Context): String? {
        return getPreferences(context).getString(TOKEN_KEY, null)
    }

    // Eliminar el token
    fun clearToken(context: Context) {
        val editor = getPreferences(context).edit()
        editor.remove(TOKEN_KEY)
        editor.apply()
    }
}
