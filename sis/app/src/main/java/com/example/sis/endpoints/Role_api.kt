package com.example.sis.endpoints

import com.example.sis.datamodels.Role
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface Role_api {
    @POST("/new_role/")
    suspend fun newRole(
        @Body role: Role
    ): Role

    @GET("/all_role")
    suspend fun allRole(): Role

    @GET("/role/{id}")
    suspend fun  getRoleId(
        @Path("id") id: Int
    )
}