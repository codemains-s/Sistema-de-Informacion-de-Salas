package com.example.sis.logic.logicRoom


import com.example.sis.conexion_api.ApiService
import com.example.sis.datamodels.RoomBooking
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import org.json.JSONObject

sealed class RegisterBookingResult {
    data class Success(val message: String) : RegisterBookingResult()
    data class Error(val message: String) : RegisterBookingResult()
}

suspend fun registerBooking(room_id: Int, user_id: Int, booking_date: String, start_time: String, end_time: String, status: String):RegisterBookingResult {
    return withContext(Dispatchers.IO){
        try {
            val newRoomBooking = RoomBooking(room_id, user_id, booking_date, start_time, end_time, status)
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