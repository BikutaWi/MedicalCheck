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
import android.util.Patterns

/*
* Project   : Medical Check Mobile
* Authors   : William Bikuta, Milán Cerviño, Ilyas Boillat, David Oktay
* Date      : 28.01.2022
* Class     : INF3dlm-a
* */

sealed class RegisterViewModelState (val message: String = "")

// return new user id on success state
class RegisterViewModelStateSuccess(var userid: Int = 0) : RegisterViewModelState("User created successfully")

class RegisterViewModelStateFailure(message: String) : RegisterViewModelState(message)

class RegisterViewModel : ViewModel() {

    private val state = MutableLiveData<RegisterViewModelState>()
    fun getState() : LiveData<RegisterViewModelState> = state

    /***
     * register new user
     */
    fun register(firstname: String, lastname: String, email: String, confirm: String, username: String, password: String, isCareKeeper: Boolean) {

        // if password and confirm match
        if (password == confirm)
        {
            // if email is valid
            if (Patterns.EMAIL_ADDRESS.matcher(email).matches())
            {
                try {

                    // create user model
                    var user: User = User();
                    user.username = username
                    user.email = email
                    user.password = password.md5()
                    user.firstname = firstname
                    user.lastname = lastname
                    user.iscarekeeper = isCareKeeper

                    // get retrofit instance and create service
                    val retrofit = Utils.retrofitInstance
                    val service = retrofit?.create(UsersService::class.java)

                    // load create user query
                    val userRequest = service?.create(user)

                    // enqueue query response
                    userRequest?.enqueue(object : Callback<User> {
                        override fun onResponse(call: Call<User>, response: Response<User>) {

                            // take user model from query response
                            val userNew = response.body()

                            // if new user exists
                            if (userNew != null)
                            {
                                // update user id and return success state
                                val success = RegisterViewModelStateSuccess()
                                success.userid = userNew.id!!
                                state.value = success
                            }
                            else
                            {
                                state.value = RegisterViewModelStateFailure("User not created: username or email is already used")
                            }
                        }

                        override fun onFailure(call: Call<User>, t: Throwable) {
                            state.value = RegisterViewModelStateFailure("User not created: username or email is already used")
                        }

                    })
                }
                catch (e: Exception) {
                    state.value = RegisterViewModelStateFailure("User not created an error occurred")
                }
            }
            else
            {
                state.value = RegisterViewModelStateFailure("Email not valid")
            }
        }
        else
        {
            state.value = RegisterViewModelStateFailure("Password and confirmation do not match")
        }
    }
}