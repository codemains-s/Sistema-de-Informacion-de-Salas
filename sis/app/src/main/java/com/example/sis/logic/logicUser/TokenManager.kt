package com.example.sis.logic.logicUser

import android.content.Context
import android.content.SharedPreferences

class TokenManager(context: Context) {

    val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    // Guardar el token
    fun saveToken(token: String) {
        sharedPreferences.edit().putString("auth_token", token).apply()
    }

    // Obtener el token
    fun getToken(): String? {
        return sharedPreferences.getString("auth_token", null)
    }

    // Eliminar el token (logout)
    fun clearToken() {
        sharedPreferences.edit().remove("auth_token").apply()
    }
}
