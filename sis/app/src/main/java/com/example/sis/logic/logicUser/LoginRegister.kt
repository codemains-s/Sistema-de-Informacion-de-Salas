package com.example.sis.logic.user.logicUser

import com.example.sis.conexion_api.ApiService
import com.example.sis.datamodels.user.UserCreate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import org.json.JSONObject

sealed class RegisterResult {
    data class Success(val message: String) : RegisterResult()
    data class Error(val message: String) : RegisterResult()
}

suspend fun registerUser(name: String, email: String, phone: String, password: String, program_id: Int, codeAdmin: String):RegisterResult {
    return withContext(Dispatchers.IO){
        try {
            val newUser = UserCreate(name, email, phone, password, program_id)
            // Hacer la llamada al API para registrar el usuario
            val response = ApiService.userApi.register_user(newUser, code = codeAdmin)
            if (response != null) {
                RegisterResult.Success("Registro exitoso")
            } else {
                RegisterResult.Error("Error desconocido en el registro")
            }
        }catch (e: HttpException){
            val errorBody = e.response()?.errorBody()?.string()
            val errorMessage = try {
                val jsonError = JSONObject(errorBody ?: "")
                jsonError.getString("detail")
            } catch (e: Exception) {
                "Error en el inicio de sesión"
            }
            RegisterResult.Error(errorMessage)
        }catch (e: Exception) {
            RegisterResult.Error(e.message ?: "Error desconocido")
        }
    }
}
