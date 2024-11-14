// LoginLogic.kt
package com.example.sis.logic.user.logicUser

import com.example.sis.conexion_api.ApiService
import com.example.sis.datamodels.user.UserLogin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import org.json.JSONObject

sealed class LoginResult {
    data class Success(val token: String) : LoginResult()
    data class Error(val message: String) : LoginResult()
}

suspend fun loginUser(email: String, password: String): LoginResult {
    return withContext(Dispatchers.IO) {
        try {
            val userLogin = UserLogin(email, password)
            val response = ApiService.userApi.loginUser(userLogin)
            if (response != null) {
                ApiService.setAuthToken(response.Token)
                LoginResult.Success(response.Token)

            } else {
                LoginResult.Error("Error desconocido en el inicio de sesión")
            }
        } catch (e: HttpException) {
            // Extraer el mensaje de detail del cuerpo de la respuesta
            val errorBody = e.response()?.errorBody()?.string()
            val errorMessage = try {
                val jsonError = JSONObject(errorBody ?: "")
                jsonError.getString("detail")
            } catch (e: Exception) {
                "Error en el inicio de sesión"
            }
            LoginResult.Error(errorMessage)
        } catch (e: Exception) {
            LoginResult.Error(e.message ?: "Error desconocido")
        }
    }
}