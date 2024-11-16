package com.example.sis.logic.logicRoom

import android.content.Context
import com.example.sis.conexion_api.ApiService
import com.example.sis.datamodels.room.Room
import com.example.sis.logic.logicUser.TokenManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response

sealed class RoomResult {
    data class Success(val rooms: List<Room>) : RoomResult()
    data class Error(val message: String) : RoomResult()
}

suspend fun roomList(context: Context): RoomResult {
    val token = TokenManager(context).getToken() // Obtener el token desde SharedPreferences
    return withContext(Dispatchers.IO) {
        try {
            // Si el token es null o vacío, puedes manejarlo aquí (puedes lanzar un error o simplemente no enviar el token)
            if (token.isNullOrEmpty()) {
                return@withContext RoomResult.Error("Token no disponible")
            }

            // Hacer la solicitud a la API con el token en los encabezados
            val response: Response<List<Room>> = ApiService.roomApi.get_rooms("Bearer $token")

            if (response.isSuccessful) {
                response.body()?.let { rooms ->
                    RoomResult.Success(rooms)
                } ?: RoomResult.Error("No se encontraron salas")
            } else {
                RoomResult.Error("Error en la API: ${response.message()}")
            }
        } catch (e: HttpException) {
            RoomResult.Error("Error en la API: ${e.message()}")
        } catch (e: Exception) {
            RoomResult.Error(e.message ?: "Error desconocido")
        }
    }
}
