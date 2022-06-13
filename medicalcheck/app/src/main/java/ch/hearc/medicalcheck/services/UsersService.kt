package ch.hearc.medicalcheck.services

import ch.hearc.medicalcheck.models.User
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

/*
* Project   : Medical Check Mobile
* Authors   : William Bikuta, Milán Cerviño, Ilyas Boillat, David Oktay
* Date      : 28.01.2022
* Class     : INF3dlm-a
* */

interface  UsersService {
    @GET("users")
    fun getAll(): Call<List<User>>

    @GET("users/{id}")
    fun get(@Path("id") id : Int): Call<User>

    @GET("users/login/{username}")
    fun get(@Path("username") username : String): Call<User>

    @GET("users/carekeeper/{iduser}")
    fun getAllAvailableCarekeeper(@Path("iduser") iduser: Int): Call<List<User>>

    @POST("users")
    fun create(@Body user: User): Call<User>

    @DELETE("users/{id}")
    fun delete(@Path("id") id: Int): Call<ResponseBody>

    @PUT("users/{id}")
    fun update(@Path("id") id : Int, @Body user: User): Call<User>

    @GET("codes/generate/{id}")
    fun generateAndGetCode(@Path("id") id : Int): Call<Int>

    @GET("codes/user/{code}")
    fun getByCode(@Path("code") code : Int): Call<User>
}