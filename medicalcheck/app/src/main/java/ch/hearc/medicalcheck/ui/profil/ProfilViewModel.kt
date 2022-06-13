package ch.hearc.medicalcheck.ui.profil

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ch.hearc.medicalcheck.models.User
import ch.hearc.medicalcheck.services.UsersService
import ch.hearc.medicalcheck.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/*
* Project   : Medical Check Mobile
* Authors   : William Bikuta, Milán Cerviño, Ilyas Boillat, David Oktay
* Date      : 28.01.2022
* Class     : INF3dlm-a
* */

class ProfilViewModel : ViewModel() {

    private val names = MutableLiveData<String>()
    private val email = MutableLiveData<String>()
    private val code = MutableLiveData<String>()

    private val id = MutableLiveData<Int>()
    fun getNames() : LiveData<String> = names
    fun getEmail() : LiveData<String> = email
    fun getCode() : LiveData<String> = code

    fun getId() : LiveData<Int> = id

    /***
     * get user instance form rest service and update firstname, email and user id
     */
    fun loadUser() {

        // get connected user's id
        val userid: Int = Utils.userId

        // get retrofit instance and create service
        val retrofit = Utils.retrofitInstance
        val service = retrofit?.create(UsersService::class.java)

        // get user from Rest service
        val userRequest = service?.get(userid)

        // enqueue query response
        userRequest?.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {

                // take user model from query response
                val user = response.body()

                // if user exist
                if (user != null)
                {
                    // update data
                    names.value = user.firstname.toString() + " " + user.lastname.toString()
                    email.value = user.email.toString()
                    id.value = user.id
                }
                else
                {
                    error("user doesn't exist")
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                error("user doesn't exist")
            }
        })
    }

    /***
     * get user instance form rest service and update firstname, email and user id
     */
    fun loadCode() {

        // get connected user's id
        val userid: Int = Utils.userId

        // get retrofit instance and create service
        val retrofit = Utils.retrofitInstance
        val service = retrofit?.create(UsersService::class.java)

        // get query from Rest service
        val userRequest = service?.generateAndGetCode(userid)

        // enqueue query response
        userRequest?.enqueue(object : Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {

                // take user model from query response
                val codeRsp = response.body()

                // if user exist
                if (codeRsp != null)
                {
                    // update data
                    code.value = "Code : " + codeRsp.toString();
                }
                else
                {
                    error("Code could not be created")
                }
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
                error(t.message.toString())
            }
        })
    }
}