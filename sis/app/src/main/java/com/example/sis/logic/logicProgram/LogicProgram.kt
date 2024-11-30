package com.example.sis.logic.logicProgram

import android.content.Context
import com.example.sis.conexion_api.ApiService
import com.example.sis.datamodels.program.Program
import com.example.sis.datamodels.program.ProgramById
import com.example.sis.logic.logicRoom.RoomResult
import com.example.sis.logic.logicUser.TokenManager
import com.example.sis.logic.user.logicUser.RegisterResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

sealed class ProgramRegister {
    data class Success(val programs: Program) : ProgramRegister()
    data class Error(val message: String) : ProgramRegister()
}
suspend fun registerProgram(name: String, description: String, context: Context): ProgramRegister{
    val token = TokenManager(context).getToken()
    return withContext(Dispatchers.IO) {
        try {
            if (token.isNullOrEmpty()) {
                return@withContext ProgramRegister.Error("Token no disponible")
            }
            val program = Program(name, description)
            val response = ApiService.programApi.newProgram(program, "Bearer $token")
            if (response != null) {
                ProgramRegister.Success(response)
            } else {
                ProgramRegister.Error("Error desconocido en el registro")
            }

        } catch (e: HttpException) {
            ProgramRegister.Error("Error en la API: ${e.message()}")
        } catch (e: Exception) {
            ProgramRegister.Error(e.message ?: "Error desconocido")
        } as ProgramRegister
    }
}




sealed class ProgramResult {
    data class Success(val programs: List<ProgramById>) : ProgramResult()
    data class Error(val message: String) : ProgramResult()
}

suspend fun programList(context: Context): ProgramResult {
    val token = TokenManager(context).getToken()
    return withContext(Dispatchers.IO) {
        try {
            if (token.isNullOrEmpty()) {
                return@withContext ProgramResult.Error("Token no disponible")
            }
            val programs: List<ProgramById> = ApiService.programApi.allPrograms("Bearer $token")

            if (programs.isEmpty()) {
                return@withContext ProgramResult.Error("No se encontraron programas")
            }
            ProgramResult.Success(programs)
        } catch (e: HttpException) {
            ProgramResult.Error("Error en la API: ${e.message()}")
        } catch (e: Exception) {
            ProgramResult.Error(e.message ?: "Error desconocido")
        } as ProgramResult
    }
}