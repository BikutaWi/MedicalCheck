package ch.hearc.medicalcheck.services

import ch.hearc.medicalcheck.models.Treatment
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

interface  TreatmentService {
    @GET("traitements")
    fun getAll(): Call<List<Treatment>>

    @GET("traitements/{id}")
    fun get(@Path("id") id : Int): Call<Treatment>

    @GET("traitements/user/{id}")
    fun getAllByIdUser(@Path("id") id : Int): Call<List<Treatment>>

    @GET("traitements/count/{iduser}")
    fun countTreatmentNotTaken(@Path("iduser") iduser: Int): Call<Int>

    @POST("traitements")
    fun create(@Body traitements: Treatment): Call<Treatment>

    @DELETE("traitements/{id}")
    fun delete(@Path("id") id: Int): Call<ResponseBody>

    @PUT("traitements/{id}")
    fun update(@Path("id") id : Int, @Body traitement: Treatment): Call<Treatment>
}