// AllUsers.kt
package com.example.sis.logic.logicUser

import android.content.Context
import com.example.sis.conexion_api.ApiService
import com.example.sis.datamodels.user.User
import com.example.sis.datamodels.user.UserTable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import exceptionsAlert


sealed class AllUsersResult {
    data class Success(val users: List<User>) : AllUsersResult()
    data class Error(val message: String) : AllUsersResult()
}

suspend fun getAllUsers(context: Context): AllUsersResult {
    val token = TokenManager(context).getToken()
    return withContext(Dispatchers.IO) {
        try {
            if (token.isNullOrEmpty()) {
                return@withContext AllUsersResult.Error("Token no disponible")
            }

            val response: Response<List<User>> = ApiService.userApi.get_all_users_role_id(2,"Bearer ${token}")

            if (response.isSuccessful) {
                response.body()?.let { users ->
                    AllUsersResult.Success(users)
                } ?: AllUsersResult.Error("No se encontraron salas")
            } else {
                AllUsersResult.Error("No tienes los permisos necesarios")
            }
        } catch (e: HttpException) {
            if (e.code() == 403) {
                // Verificamos si el error es un 403 (permiso denegado)
                if (e.message()?.contains("No cuentas con permisos") == true) {
                    exceptionsAlert(context) // Mostrar el popup cuando no hay permisos
                }
            }
            AllUsersResult.Error("No tienes los permisos necesarios")
        } catch (e: Exception) {
            AllUsersResult.Error(e.message ?: "Error desconocido")
        } as AllUsersResult
    }
}

sealed class AllUsersRoleIdResult {
    data class Success(val users: List<User>) : AllUsersRoleIdResult()
    data class Error(val message: String) : AllUsersRoleIdResult()
}

suspend fun getAllUsersRoleId(context: Context): AllUsersRoleIdResult {
    val token = TokenManager(context).getToken()
    return withContext(Dispatchers.IO) {
        try {
            if (token.isNullOrEmpty()) {
                return@withContext AllUsersRoleIdResult.Error("Token no disponible")
            }

            val response: Response<List<User>> = ApiService.userApi.get_all_users_role_id(2,"Bearer ${token}")

            if (response.isSuccessful) {
                response.body()?.let { users ->
                    AllUsersRoleIdResult.Success(users)
                } ?: AllUsersRoleIdResult.Error("No se encontraron usuarios")
            } else {
                AllUsersRoleIdResult.Error("No tienes los permisos necesarios")
            }
        } catch (e: HttpException) {
            if (e.code() == 403) {
                // Verificamos si el error es un 403 (permiso denegado)
                if (e.message()?.contains("No cuentas con permisos") == true) {
                    exceptionsAlert(context) // Mostrar el popup cuando no hay permisos
                }
            }
            AllUsersRoleIdResult.Error("No tienes los permisos necesarios")
        } catch (e: Exception) {
            AllUsersRoleIdResult.Error(e.message ?: "Error desconocido")
        } as AllUsersRoleIdResult
    }
}