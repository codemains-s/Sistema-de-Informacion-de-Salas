// AllUsers.kt
package com.example.sis.logic.logicUser

import android.content.Context
import com.example.sis.conexion_api.ApiService
import com.example.sis.datamodels.user.UserTable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response

sealed class AllUsersResult {
    data class Success(val users: List<UserTable>) : AllUsersResult()
    data class Error(val message: String) : AllUsersResult()
}

suspend fun getAllUsers(context: Context): AllUsersResult {
    val token = TokenManager(context).getToken()
    return withContext(Dispatchers.IO) {
        try {

            val response: Response<List<UserTable>> = ApiService.userApi.get_all_users("Bearer ${token}")

            if (response.isSuccessful) {
                response.body()?.let { users ->
                    AllUsersResult.Success(users)
                } ?: AllUsersResult.Error("No se encontraron salas")
            } else {
                AllUsersResult.Error("Error en la API: ${response.message()}")
            }
        } catch (e: HttpException) {
            AllUsersResult.Error("Error en la API: ${e.message()}")
        } catch (e: Exception) {
            AllUsersResult.Error(e.message ?: "Error desconocido")
        }
    }
}