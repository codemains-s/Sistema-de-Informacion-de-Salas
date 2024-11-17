package com.example.sis.conexion_api

import com.example.sis.endpoints.RoomBooking_api
import com.example.sis.endpoints.Room_api
import com.example.sis.endpoints.User_api
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiService {
    private var baseUrl = "http://192.168.1.105:8000/" // Default IP

    private val retrofit: Retrofit
        get() = Retrofit.Builder()
            .baseUrl(baseUrl)

            .addConverterFactory(GsonConverterFactory.create())
            .build()

    fun updateBaseUrl(ip: String) {
        baseUrl = "http://$ip:8000/"
    }


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
        var authToken = token
    }

    val roomBookingApi: RoomBooking_api
        get() = retrofit.create(RoomBooking_api::class.java)

}
