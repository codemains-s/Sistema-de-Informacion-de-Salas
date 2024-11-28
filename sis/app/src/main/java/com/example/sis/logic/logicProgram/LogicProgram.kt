package com.example.sis.logic.logicProgram

import android.content.Context
import com.example.sis.conexion_api.ApiService
import com.example.sis.datamodels.program.Program
import com.example.sis.datamodels.room.Room
import com.example.sis.logic.logicUser.TokenManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response


sealed class ProgramResult {
    data class Success(val programs: List<Program>) : ProgramResult()
    data class Error(val message: String) : ProgramResult()
}

suspend fun programList(context: Context): ProgramResult {
    val token = TokenManager(context).getToken()
    return withContext(Dispatchers.IO) {
        try {
            val programs: List<Program> = ApiService.programApi.allPrograms()

            if (programs.isEmpty()) {
                return@withContext ProgramResult.Error("No se encontraron programas")
            }
            ProgramResult.Success(programs)
        } catch (e: HttpException) {
            ProgramResult.Error("Error en la API: ${e.message()}")
        } catch (e: Exception) {
            ProgramResult.Error(e.message ?: "Error desconocido")
        }
    }
}