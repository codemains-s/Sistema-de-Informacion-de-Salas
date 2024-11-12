package com.example.sis.conexion_api

import com.example.sis.endpoints.User_api
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiService {
    private const val BASE_URL = "http://192.168.1.103:8000/" //Url API

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val userApi: User_api by lazy {
        retrofit.create(User_api::class.java)
    }}