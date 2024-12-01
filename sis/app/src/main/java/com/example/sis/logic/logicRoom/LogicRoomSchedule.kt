package com.example.sis.logic.logicRoom

import android.content.Context
import com.example.sis.conexion_api.ApiService
import com.example.sis.datamodels.RoomSchedule
import com.example.sis.datamodels.RoomScheduleId
import com.example.sis.logic.logicUser.TokenManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response

sealed class RegisterRoomSheduleResult {
    data class Success(val message: String) : RegisterRoomSheduleResult()
    data class Error(val message: String) : RegisterRoomSheduleResult()
}

suspend fun registerRoomShedule(room_id: Int, hour_start: String, hour_end: String, day_of_week: String, status:String, context: Context):RegisterRoomSheduleResult {
    val token = TokenManager(context).getToken()
    return withContext(Dispatchers.IO) {
        try {
            if (token.isNullOrEmpty()) {
                return@withContext RegisterRoomSheduleResult.Error("Token no disponible")
            }
            val newRoomSchedule = RoomSchedule(room_id, hour_start, hour_end, day_of_week, status)
            val response: RoomSchedule = ApiService.roomScheduleApi.newRoomSchedule(newRoomSchedule, "Bearer $token")

            if (response != null) {
                RegisterRoomSheduleResult.Success("Registro exitoso")
            } else {
                RegisterRoomSheduleResult.Error("Error desconocido en el registro")
            }
        } catch (e: HttpException) {
            RegisterRoomSheduleResult.Error("Error en la API: ${e.message()}")
        } catch (e: Exception) {
            RegisterRoomSheduleResult.Error(e.message ?: "Error desconocido")
        }
    }
}

sealed class RoomScheduleResult {
    data class Success(val rooms: List<RoomScheduleId>) : RoomScheduleResult()
    data class Error(val message: String) : RoomScheduleResult()
}

suspend fun roomScheduleListId(id: Int, context: Context): RoomScheduleResult {
    val token = TokenManager(context).getToken()
    return withContext(Dispatchers.IO) {
        try {

            val response: Response<List<RoomScheduleId>> = ApiService.roomScheduleApi.getRoomScheduleByRoomId(id)

            if (response.isSuccessful) {
                response.body()?.let { roomSchedules ->
                    RoomScheduleResult.Success(roomSchedules)
                } ?: RoomScheduleResult.Error("No se encontraron horarios")
            } else {
                RoomScheduleResult.Error("Error en la API: ${response.message()}")
            }
        } catch (e: HttpException) {
            RoomScheduleResult.Error("Error en la API: ${e.message()}")
        } catch (e: Exception) {
            RoomScheduleResult.Error(e.message ?: "Error desconocido")
        }
    }
}