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

/*
* Project   : Medical Check Mobile
* Authors   : William Bikuta, Milán Cerviño, Ilyas Boillat, David Oktay
* Date      : 28.01.2022
* Class     : INF3dlm-a
* */

sealed class LoginViewModelState (val message: String = "")

// success return user id to insert in static variable
class LoginViewModelStateSuccess(var userid: Int = 0) : LoginViewModelState("User logged successfully") {
    private var userId: Int

    get() {
        return userId
    }
    set(value) {
        userId = value
    }
}

class LoginViewModelStateFailure(message: String) : LoginViewModelState(message)

class LoginViewModel : ViewModel() {
    private val state = MutableLiveData<LoginViewModelState>()
    fun getState() : LiveData<LoginViewModelState> = state

    /***
     * try to log in user
     */
    fun login(username: String, password: String) {
        try {

            // get retrofit instance and create service
            val retrofit = Utils.retrofitInstance
            val service = retrofit?.create(UsersService::class.java)

            // get user by username
            val userRequest = service?.get(username)

            // enqueue query response
            userRequest?.enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {

                    // take user model from repsonse
                    val user = response.body()

                    // if user exist
                    if (user != null) {

                        // hash password
                        val hash = password.md5()

                        // if password is correct
                        if (hash.equals(user.password)) {

                            // update state
                            val success = LoginViewModelStateSuccess()
                            success.userid = user.id!!
                            state.value = success

                        } else {
                            state.value = LoginViewModelStateFailure("Incorrect username or password")
                        }
                    } else {
                        state.value = LoginViewModelStateFailure("Unknown username")
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    state.value = LoginViewModelStateFailure("Unknown username")
                }
            })
        }
        catch (e: Exception) {
            state.value = LoginViewModelStateFailure("Error with database")
        }
    }
}