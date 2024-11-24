package com.example.sis.endpoints

import com.example.sis.datamodels.room.Room
import com.example.sis.datamodels.user.User
import com.example.sis.datamodels.user.UserCreate
import com.example.sis.datamodels.user.UserLogin
import com.example.sis.datamodels.user.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface User_api {
    @POST("/register_user/")
    suspend fun register_user(
        @Body newUser: UserCreate,
        @Query("code") code: String =""
    ): User

    @POST("/login_user/")
    suspend fun loginUser(
        @Body user: UserLogin
    ): UserResponse

    @GET("/user_by_id/")
    suspend fun get_user_by_id(@Query("id") id: Int,
                               @Header("Authorization") authHeader: String
    ): Response<User>

}