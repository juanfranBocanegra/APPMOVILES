package com.app.dolt.utils

import android.content.Context
import android.content.SharedPreferences

/**
 * Objeto singleton que gestiona el almacenamiento y recuperación del token de autenticación
 * utilizando [SharedPreferences].
 */
object SharedPreferencesManager {

    private const val PREFS_NAME = "user_preferences"
    private const val TOKEN_KEY = "BEARER_TOKEN"

    /**
     * Obtiene las [SharedPreferences] de la aplicación.
     * 
     * @param context : Contexto de la aplicación.
     * @return Instancia de [SharedPreferences].
     */
    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    /**
     * Guarda el token de autenticación en las preferencias compartidas.
     * 
     * @param context : Contexto de la aplicación.
     * @param token : Token de autenticación a guardar.
     */
    fun saveToken(context: Context, token: String) {
        val editor = getPreferences(context).edit()
        editor.putString(TOKEN_KEY, token)
        editor.apply()
    }

    /**
     * Obtiene el token de autenticación almacenado.
     * 
     * @param context : Contexto de la aplicación.
     * @return Token de autenticación o 'null' si no existe.
     */
    fun getToken(context: Context): String? {
        return getPreferences(context).getString(TOKEN_KEY, null)
    }

    /**
     * Elimina el token de autenticación almacenado.
     * 
     * @param context : Contexto de la aplicación.
     */
    fun clearToken(context: Context) {
        val editor = getPreferences(context).edit()
        editor.remove(TOKEN_KEY)
        editor.apply()
    }
}
