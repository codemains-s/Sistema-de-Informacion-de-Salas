package com.example.sis.conexion_api

import com.example.sis.endpoints.RoomBooking_api
import com.example.sis.endpoints.Room_api
import com.example.sis.endpoints.User_api
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiService {
    private var baseUrl = "http://192.168.1.22:8000/" // Default IP

    private val retrofit: Retrofit
        get() = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    fun updateBaseUrl(ip: String) {
        baseUrl = "http://$ip:8000/"
    }

    val userApi: User_api
        get() = retrofit.create(User_api::class.java)

    val roomApi: Room_api
        get() = retrofit.create(Room_api::class.java)

    val roomBookingApi: RoomBooking_api
        get() = retrofit.create(RoomBooking_api::class.java)
}
