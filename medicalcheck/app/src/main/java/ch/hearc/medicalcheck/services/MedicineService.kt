package ch.hearc.medicalcheck.services

import ch.hearc.medicalcheck.models.Medicine
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

/*
* Project   : Medical Check Mobile
* Authors   : William Bikuta, Milán Cerviño, Ilyas Boillat, David Oktay
* Date      : 28.01.2022
* Class     : INF3dlm-a
* */

interface  MedicineService {
    @GET("medicines")
    fun getAll(): Call<List<Medicine>>

    @GET("medicines/{id}")
    fun get(@Path("id") id : Int): Call<Medicine>

    @GET("medicines/user/{id}")
    fun getAllByIdUser(@Path("id") id : Int): Call<List<Medicine>>

    @POST("medicines")
    fun create(@Body medicine: Medicine): Call<Medicine>

    @DELETE("medicines/{id}")
    fun delete(@Path("id") id: Int): Call<ResponseBody>

    @PUT("medicines/{id}")
    fun update(@Path("id") id : Int, @Body medicine: Medicine): Call<Medicine>
}