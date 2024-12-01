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
    val token = TokenManager(context).getToken()
    return withContext(Dispatchers.IO) {
        try {

            val response: Response<List<Room>> = ApiService.roomApi.get_rooms()

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

suspend fun roomById(context: Context, roomId: Int): RoomResult {
    val token = TokenManager(context).getToken()
    return withContext(Dispatchers.IO) {
        try {

            val response: Response<Room> = ApiService.roomApi.get_room_by_id(roomId)

            if (response.isSuccessful) {
                response.body()?.let { room ->
                    RoomResult.Success(listOf(room))
                } ?: RoomResult.Error("No se encontró la sala")
            } else {
                RoomResult.Error("Error en la API: ${response}")
            }
        } catch (e: HttpException) {
            RoomResult.Error("Error en la API: ${e}")
        } catch (e: Exception) {
            RoomResult.Error(e.message ?: "Error desconocido")
        }
    }
}