package com.example.sis.endpoints

import com.example.sis.datamodels.room.Room
import com.example.sis.datamodels.user.User
import com.example.sis.datamodels.user.UserCreate
import com.example.sis.datamodels.user.UserLogin
import com.example.sis.datamodels.user.UserResponse
import com.example.sis.datamodels.user.UserTable
import okhttp3.ResponseBody
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

    @GET("/all_users/")
    suspend fun get_users(
        @Header("Authorization") authHeader: String
    ): Response<List<User>>

    @GET("/all_users_role_id/")
    suspend fun get_all_users_role_id(
        @Query("role_id") role_id: Int,
        @Header("Authorization") authHeader: String
    ): Response<List<UserTable>>

    @GET("/user_by_id/")
    suspend fun get_user_by_id(@Query("id") id: Int,
                               @Header("Authorization") authHeader: String
    ): Response<User>

    @GET("/all_users")
    suspend fun get_all_users(
        @Header("Authorization") authHeader: String
    ): Response<List<UserTable>>

    @GET("/user_report/{user_id}")
    suspend fun downloadUserReport(
        @Path("user_id") userId: String,
        @Header("Authorization") authHeader: String
    ): Response<ResponseBody>

}