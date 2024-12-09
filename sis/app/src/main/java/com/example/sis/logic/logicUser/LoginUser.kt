// LoginLogic.kt
package com.example.sis.logic.user.logicUser

import UserIdManager
import UserRoleIdManager
import android.content.Context
import com.example.sis.conexion_api.ApiService
import com.example.sis.datamodels.user.User
import com.example.sis.datamodels.user.UserLogin
import com.example.sis.logic.logicUser.TokenManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import org.json.JSONObject


sealed class LoginResult {
    data class Success(val token: String, val userId: String) : LoginResult()
    data class Error(val message: String) : LoginResult()
}

suspend fun loginUser(email: String, password: String, context: Context): LoginResult {
    return withContext(Dispatchers.IO) {
        try {
            val userLogin = UserLogin(email, password)
            val response = ApiService.userApi.loginUser(userLogin)

            if (response != null) {
                val token = response.Token
                val userId = response.userId
                val userRoleId = response.roleId

                TokenManager(context).saveToken(token)
                UserIdManager(context).saveUserId(userId)
                UserRoleIdManager(context).saveUserRoleId(userRoleId)

                LoginResult.Success(token, userId)
            } else {
                LoginResult.Error("Error desconocido en el inicio de sesión")
            }
        } catch (e: HttpException) {
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

suspend fun userById(context: Context, userId: Int): Result<User> {

    val token = TokenManager(context).getToken()
    if (token.isNullOrEmpty()) {
        return Result.failure(Exception("Token no disponible"))
    }
    return withContext(Dispatchers.IO) {
        try {
            val response = ApiService.userApi.get_user_by_id(userId, "Bearer $token")

            println("response $response")

            if (response.isSuccessful) {
                response.body()?.let { user ->
                    Result.success(user)
                } ?: Result.failure(Exception("El cuerpo de la respuesta está vacío"))
            } else {
                Result.failure(Exception("Error en la API: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

