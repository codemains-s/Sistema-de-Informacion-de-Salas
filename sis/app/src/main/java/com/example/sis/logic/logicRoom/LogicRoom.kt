package com.example.sis.logic.logicRoom

import com.example.sis.conexion_api.ApiService
import com.example.sis.datamodels.room.RoomCreate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import org.json.JSONObject

sealed class RoomResult {
    data class Success(val rooms: List<RoomCreate>) : RoomResult()
    data class Error(val message: String) : RoomResult()
}

suspend fun roomList(): RoomResult {
    return withContext(Dispatchers.IO) {
        try {
            val response = ApiService.roomApi.get_rooms()
            if (response != null) {
                RoomResult.Success(response)
            } else {
                RoomResult.Error("Error desconocido")
            }
        } catch (e: HttpException) {
            RoomResult.Error("Error en la API")
        } catch (e: Exception) {
            RoomResult.Error(e.message ?: "Error desconocido")
        }
    }
}
