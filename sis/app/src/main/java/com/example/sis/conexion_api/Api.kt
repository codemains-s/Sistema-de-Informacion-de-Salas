package com.example.sis.conexion_api

<<<<<<< HEAD
import android.util.Log
=======
import com.example.sis.endpoints.RoomBooking_api
>>>>>>> bd99d14f59cd0c7d2cb74b6beec4569155194a8f
import com.example.sis.endpoints.Room_api
import com.example.sis.endpoints.User_api
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiService {
<<<<<<< HEAD
    private const val BASE_URL = "http://192.168.1.22:8000/" // URL de la API

    private var authToken: String? = null

    // Interceptor para añadir el token de autorización
    private val authInterceptor = Interceptor { chain ->
        val originalRequest = chain.request()
        val builder = originalRequest.newBuilder()

        // Añadir el token a la cabecera si existe
        authToken?.let {
            builder.addHeader("Authorization", "Bearer $it")
            Log.d("API_REQUEST", "Token de autorización: Bearer $it")
        }

        val modifiedRequest = builder.build()
        chain.proceed(modifiedRequest)
    }

    // Cliente de OkHttp con el interceptor
    private val client = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .build()

    // Configuración de Retrofit con el cliente de OkHttp
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client) // Usar el cliente configurado
=======
    private var baseUrl = "http://192.168.0.11:8000/" // Default IP

    private val retrofit: Retrofit
        get() = Retrofit.Builder()
            .baseUrl(baseUrl)
>>>>>>> bd99d14f59cd0c7d2cb74b6beec4569155194a8f
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    fun updateBaseUrl(ip: String) {
        baseUrl = "http://$ip:8000/"
    }

<<<<<<< HEAD
    // API de usuario
    val userApi: User_api by lazy {
        retrofit.create(User_api::class.java)
    }

    // API de habitaciones
    val roomApi: Room_api by lazy {
        retrofit.create(Room_api::class.java)
    }

    // Función para actualizar el token
    fun setAuthToken(token: String) {
        authToken = token
    }
=======
    val userApi: User_api
        get() = retrofit.create(User_api::class.java)

    val roomApi: Room_api
        get() = retrofit.create(Room_api::class.java)

    val roomBookingApi: RoomBooking_api
        get() = retrofit.create(RoomBooking_api::class.java)
>>>>>>> bd99d14f59cd0c7d2cb74b6beec4569155194a8f
}
