package com.example.sis.logic.logicRecordHours

import android.content.Context
import com.example.sis.conexion_api.ApiService
import com.example.sis.datamodels.RecordHours.RecordHours
import com.example.sis.logic.logicUser.TokenManager
import exceptionsAlert
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

sealed class RecordHoursRegister {
    data class Success(val message: RecordHours) : RecordHoursRegister()
    data class Error(val message: String) : RecordHoursRegister()
}

suspend fun registrarHoras(horasCumplidas: RecordHours, context: Context): RecordHoursRegister {
    val token = TokenManager(context).getToken()
    val response = ApiService.recordHoursApi.registrarHoras(horasCumplidas, "Bearer $token")

    return withContext(Dispatchers.IO) {
        try {
            if (token.isNullOrEmpty()) {
                return@withContext RecordHoursRegister.Error("Token no disponible")
            }
            if (response != null) {
                RecordHoursRegister.Success(response)
            } else {
                RecordHoursRegister.Error("Error desconocido en el registro")
            }
        } catch (e: HttpException) {
            if (e.code() == 403) {
                // Verificamos si el error es un 403 (permiso denegado)
                if (e.message()?.contains("No cuentas con permisos") == true) {
                    exceptionsAlert(context) // Mostrar el popup cuando no hay permisos
                }
            }
            RecordHoursRegister.Error("No tienes los permisos necesarios")
        } catch (e: Exception) {
            exceptionsAlert(context) // Mostrar el popup en caso de cualquier otro error
            RecordHoursRegister.Error("No tienes los permisos necesarios")
        }
    }
}

