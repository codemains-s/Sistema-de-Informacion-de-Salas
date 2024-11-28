package com.example.sis.logic.logicRoom


import android.content.Context
import com.example.sis.conexion_api.ApiService
import com.example.sis.datamodels.RoomBooking
import com.example.sis.datamodels.room.Room
import com.example.sis.logic.logicUser.TokenManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import org.json.JSONObject
import retrofit2.Response

sealed class RegisterBookingResult {
    data class Success(val message: String) : RegisterBookingResult()
    data class Error(val message: String) : RegisterBookingResult()
}



suspend fun registerBooking(user_id: Int,room_id: Int, booking_date: String, start_time: String, end_time: String, status: String):RegisterBookingResult {
    return withContext(Dispatchers.IO){
        try {
            val newRoomBooking = RoomBooking(user_id, room_id, booking_date, start_time, end_time, status)
            val response = ApiService.roomBookingApi.newRoomBooking(newRoomBooking)
            if (response != null) {
                RegisterBookingResult.Success("Registro exitoso")
            } else {
                RegisterBookingResult.Error("Error desconocido en el registro")
            }
        }catch (e: HttpException){
            val errorBody = e.response()?.errorBody()?.string()
            val errorMessage = try {
                val jsonError = JSONObject(errorBody ?: "")
                jsonError.getString("detail")
            } catch (e: Exception) {
                "Error en la asignación de horario"
            }
            RegisterBookingResult.Error(errorMessage)
        }catch (e: Exception) {
            RegisterBookingResult.Error(e.message ?: "Error desconocido")
        }
    }
}

sealed class RoomBookingResult {
    data class Success(val rooms: List<RoomBooking>) : RoomBookingResult()
    data class Error(val message: String) : RoomBookingResult()
}

suspend fun roomBookingList(context: Context): RoomBookingResult {
    val token = TokenManager(context).getToken()
    return withContext(Dispatchers.IO) {
        try {
            if (token.isNullOrEmpty()) {
                return@withContext RoomBookingResult.Error("Token no disponible")
            }

            val response: Response<List<RoomBooking>> = ApiService.roomBookingApi.allRoomBooking("Bearer $token")

            if (response.isSuccessful) {
                response.body()?.let { roomBookings ->
                    RoomBookingResult.Success(roomBookings)
                } ?: RoomBookingResult.Error("No se encontraron salas")
            } else {
                RoomBookingResult.Error("Error en la API: ${response.message()}")
            }
        } catch (e: HttpException) {
            RoomBookingResult.Error("Error en la API: ${e.message()}")
        } catch (e: Exception) {
            RoomBookingResult.Error(e.message ?: "Error desconocido")
        }
    }
}