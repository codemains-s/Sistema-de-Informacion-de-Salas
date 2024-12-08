package com.example.sis.logic.logicUser

import android.content.Context
import com.example.sis.conexion_api.ApiService
import com.example.sis.datamodels.user.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response

sealed class UserResult {
    data class Success(val users: List<User>) : UserResult()
    data class Error(val message: String) : UserResult()
}

suspend fun userList(context: Context): UserResult {
    val token = TokenManager(context).getToken()
    return withContext(Dispatchers.IO) {
        try {
            if (token.isNullOrEmpty()) {
                return@withContext UserResult.Error("Token no disponible")
            }

            val response: Response<List<User>> = ApiService.userApi.get_users("Bearer $token")

            if (response.isSuccessful) {
                response.body()?.let { users ->
                    UserResult.Success(users)
                } ?: UserResult.Error("No se encontraron usuarios")
            } else {
                UserResult.Error("Error en la API: ${response.message()}")
            }
        } catch (e: HttpException) {
            UserResult.Error("Error en la API: ${e.message()}")
        } catch (e: Exception) {
            UserResult.Error(e.message ?: "Error desconocido")
        }
    }
}

suspend fun userById(context: Context, userId: Int): UserResult {
    val token = TokenManager(context).getToken()
    return withContext(Dispatchers.IO) {
        try {
            if (token.isNullOrEmpty()) {
                return@withContext UserResult.Error("Token no disponible")
            }

            val response: Response<User> = ApiService.userApi.get_user_by_id(userId, "Bearer $token")

            if (response.isSuccessful) {
                response.body()?.let { user ->
                    UserResult.Success(listOf(user))
                } ?: UserResult.Error("No se encontró el usuario")
            } else {
                UserResult.Error("Error en la API: ${response.message()}")
            }
        } catch (e: HttpException) {
            UserResult.Error("Error en la API: ${e.message()}")
        } catch (e: Exception) {
            UserResult.Error(e.message ?: "Error desconocido")
        }
    }
}
