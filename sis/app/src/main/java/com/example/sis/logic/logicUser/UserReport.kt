package com.example.app.utils

import android.content.Context
import android.os.Environment
import com.example.sis.conexion_api.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

sealed class UserReportResult {
    data class Success(val filePath: String) : UserReportResult()
    data class Error(val message: String) : UserReportResult()
}

suspend fun downloadUserReport(
    context: Context,
    userId: String,
    authHeader: String
): UserReportResult {
    return withContext(Dispatchers.IO) {
        try {
            val response = ApiService.userApi.downloadUserReport(userId, authHeader)

            if (!response.isSuccessful) {
                return@withContext UserReportResult.Error("Error en la API: ${response.message()}")
            }

            val responseBody = response.body()
                ?: return@withContext UserReportResult.Error("Respuesta vacía del servidor")

            val fileName = "user_report_$userId.xlsx"
            val downloadsDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val file = File(downloadsDir, fileName)

            responseBody.byteStream().use { inputStream ->
                FileOutputStream(file).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }

            UserReportResult.Success(file.absolutePath)
        } catch (e: Exception) {
            e.printStackTrace()
            UserReportResult.Error(e.message ?: "Error desconocido")
        }
    }
}
