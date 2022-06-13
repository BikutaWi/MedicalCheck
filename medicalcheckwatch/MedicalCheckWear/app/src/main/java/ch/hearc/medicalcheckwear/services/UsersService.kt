package ch.hearc.medicalcheckwear.services

import ch.hearc.medicalcheckwear.models.User
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*
/*
* Project   : Medical Check Watch
* Authors   : William Bikuta, Milán Cerviño, Ilyas Boillat, David Oktay
* Date      : 28.01.2022
* Class     : INF3dlm-a
* */

/**
 * interface which allow to create a request to manage the users in our database
 */
interface  UsersService {

    /*
    * get a user when it's call, a code need to be attached to the body
    * if the code is correct, the user is return, else null
    * this method is used to login quickly, avoiding to write the username and password,
    * because it's not easy to type a lot of information from the watch
    * the code need to be generated from the mobile app, in profil view
    * the code is available 30s
    */
    @GET("codes/user/{code}")
    fun getByCode(@Path("code") code : Int): Call<User>
}