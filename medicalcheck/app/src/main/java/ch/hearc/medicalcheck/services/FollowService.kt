package ch.hearc.medicalcheck.services

import ch.hearc.medicalcheck.models.Follow
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

/*
* Project   : Medical Check Mobile
* Authors   : William Bikuta, Milán Cerviño, Ilyas Boillat, David Oktay
* Date      : 28.01.2022
* Class     : INF3dlm-a
* */

interface  FollowService {
    @GET("follows")
    fun getAll(): Call<List<Follow>>

    @GET("follows/{id}")
    fun get(@Path("id") id : Int): Call<Follow>

    @GET("follows/usercarekeeper/{idUserCareKeeper}")
    fun getAllFollowByIdUserCareKeeper(@Path("idUserCareKeeper") idUserCareKeeper: Int): Call<List<Follow>>

    @POST("follows")
    fun create(@Body follow: Follow): Call<Follow>

    @DELETE("follows/{id}")
    fun delete(@Path("id") id: Int): Call<ResponseBody>

    @PUT("follows/{id}")
    fun update(@Path("id") id : Int, @Body follow: Follow): Call<Follow>
}