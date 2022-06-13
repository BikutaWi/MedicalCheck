package ch.hearc.medicalcheck.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ch.hearc.medicalcheck.models.User
import ch.hearc.medicalcheck.services.UsersService
import ch.hearc.medicalcheck.utils.Utils
import ch.hearc.medicalcheck.utils.Utils.Companion.md5
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

/*
* Project   : Medical Check Mobile
* Authors   : William Bikuta, Milán Cerviño, Ilyas Boillat, David Oktay
* Date      : 28.01.2022
* Class     : INF3dlm-a
* */

sealed class UpdateProfilViewModelState (val message: String = "")

object UpdateProfilViewModelStateSuccess : UpdateProfilViewModelState("Profil updated successfully")

object UpdateProfilViewModelStateFailure : UpdateProfilViewModelState("Error: Profil not updated")

class UpdateProfilViewModel : ViewModel() {

    private val state = MutableLiveData<UpdateProfilViewModelState>()
    fun getState() : LiveData<UpdateProfilViewModelState> = state

    private val userData = MutableLiveData<User>()
    fun getUser() : LiveData<User> = userData

    /**
     * Load user data
     */
    fun loadUser() {
        val userid: Int = Utils.userId

        // get retrofit instance
        val retrofit = Utils.retrofitInstance

        // create service
        val service = retrofit?.create(UsersService::class.java)

        // get user's information
        val userRequest = service?.get(userid)

        userRequest?.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {

                // get query result
                val user = response.body()

                // if user exists
                if (user != null) {
                    userData.value = user!!
                } else {
                    error("user doesn't exist")
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                error("user doesn't exist")
            }
        })
    }

    /**
     * save user's data in database
     */
    fun save(user: User) {
        try {

            // get retrofit instance
            val retrofit = Utils.retrofitInstance

            // create service
            val service = retrofit?.create(UsersService::class.java)

            // update user in Rest service
            val userRequest = service?.update(user.id!!, user)

            userRequest?.enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {

                    // get query result
                    val userUpdate = response.body()

                    // if user exists
                    if (userUpdate != null)
                    {
                        // status success
                        state.value = UpdateProfilViewModelStateSuccess
                    }
                    else
                    {
                        //status fail
                        state.value = UpdateProfilViewModelStateFailure
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    state.value = UpdateProfilViewModelStateFailure
                }

            })
        }
        catch (e: Exception) {
            state.value = UpdateProfilViewModelStateFailure
        }
    }
}