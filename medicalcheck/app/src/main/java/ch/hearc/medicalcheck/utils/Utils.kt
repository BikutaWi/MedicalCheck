package ch.hearc.medicalcheck.utils

import ch.hearc.medicalcheck.models.User
import ch.hearc.medicalcheck.services.UsersService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.math.BigInteger
import java.security.MessageDigest
import retrofit2.converter.gson.GsonConverterFactory;

/*
* Project   : Medical Check Mobile
* Authors   : William Bikuta, Milán Cerviño, Ilyas Boillat, David Oktay
* Date      : 28.01.2022
* Class     : INF3dlm-a
* */

class Utils {
    companion object {

        // create gson instance and set date format to use with rest service
        @JvmStatic
        val gson : Gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create()

        // create retrofit instance
        @JvmStatic
        val retrofitInstance: Retrofit = Retrofit.Builder()
            //.baseUrl("http://192.168.56.1:8000/")
            .baseUrl("http://srvz-ct-ing-ari-p3websrvr.ing.he-arc.ch:8000/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        @JvmStatic
        var userId: Int = 0

        @JvmStatic
        var logout: Boolean = false

        @JvmStatic
        var user: User = User()

        // source = https://stackoverflow.com/questions/4846484/md5-hashing-in-android

        /***
         * hash password with md5 algorithme
         */
        fun String.md5(): String {
            val md = MessageDigest.getInstance("MD5")
            return BigInteger(1, md.digest(toByteArray())).toString(16).padStart(32, '0')
        }

        /***
         * return days array for spinner input
         */
        fun getDays(): MutableList<String> {
            val spinnerArray: MutableList<String> = ArrayList()
            spinnerArray.add("Monday")
            spinnerArray.add("Tuesday")
            spinnerArray.add("Wednesday")
            spinnerArray.add("Thursday")
            spinnerArray.add("Friday")
            spinnerArray.add("Saturday")
            spinnerArray.add("Sunday")
            spinnerArray.add("Everyday")

            return spinnerArray
        }

        fun loadUser(userid: Int = Utils.userId): User {
            val retrofit = Utils.retrofitInstance
            val service = retrofit?.create(UsersService::class.java)

            // get query from Rest service
            val userRequest = service?.get(userid)

            // enqueue query response
            userRequest?.enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    val user = response.body()
                    Utils.user = user!!
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    return error("user doesn't exists")
                }
            })

            return Utils.user
        }
    }
}