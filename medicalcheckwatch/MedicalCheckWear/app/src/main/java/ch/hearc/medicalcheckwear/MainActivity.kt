package ch.hearc.medicalcheckwear

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import android.util.Log
import android.widget.Button
import ch.hearc.medicalcheckwear.databinding.ActivityMainBinding
import ch.hearc.medicalcheckwear.models.User
import ch.hearc.medicalcheckwear.services.UsersService

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.net.ConnectivityManager
import android.widget.TextView
import android.widget.Toast
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


const val USER = "ch.hearc.medicalcheckwear.USER"
const val URL = "http://srvz-ct-ing-ari-p3websrvr.ing.he-arc.ch:8000/"

/*
* Project   : Medical Check Watch
* Authors   : William Bikuta, Milán Cerviño, Ilyas Boillat, David Oktay
* Date      : 28.01.2022
* Class     : INF3dlm-a
* */

/**
 * View allow the user to login
 * The user has to write his code, if the code is correct
 * the user is redirect to the userActivity
 */
class MainActivity : Activity() {

    private lateinit var binding: ActivityMainBinding

    //tools to indicate if the user has internet
    private var isConnected: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //when the user click on the button to login
        findViewById<Button>(R.id.btnConnexion).setOnClickListener{

            //get the code that the user has typed
            val code = findViewById<TextView>(R.id.tbxConnexion).text.toString().toInt()

            //if the user has internet
            if(isConnected)
            {
                login(code) //try to login with his information
            }
            else //case when the user has not internet
            {
                //display some usefull information
                Toast.makeText(this,"No Internet",Toast.LENGTH_SHORT).show()
            }
        }

        //check if the user has internet
        checkConnexion()

    }

    private fun checkConnexion() {

        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                // The Wi-Fi network has been acquired, bind it to use this network by default
                connectivityManager.bindProcessToNetwork(network)
                Log.i("internet","onAvailable")
                isConnected = true //indicate we have internet
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                // The Wi-Fi network has been disconnected
                Log.i("internet","onLost")
                isConnected = false //indicate we doesn't have internet
            }
        }
        connectivityManager.requestNetwork(
            NetworkRequest.Builder().addTransportType(NetworkCapabilities.TRANSPORT_WIFI).build(),
            callback
        )
    }


    /**
     * login
     *
     * check if the user has typed correct login information
     *
     * if the information are correct, the user is autorize to access to the userActivity
     * else a error message is display
     */
    fun login(code: Int) {

        //build a retrofit request
        val retro = Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        //request which is associate to the userService
        val service = retro.create(UsersService::class.java)

        //we want to get the user by his temporary login code
        val userRequest = service.getByCode(code)

        //enqueue the request, wait async to get an answer
        userRequest.enqueue(object : Callback<User> {

            //when we get an answer
            override fun onResponse(call: Call<User>, response: Response<User>) {
                val user = response.body()

                //if the user exist (login information was correct)
                if(user != null)
                {
                    //change view and transfer it the user id
                    val intent = Intent( this@MainActivity , DisplayUserActivity::class.java).apply{
                        putExtra(USER,user.id)
                    }
                    startActivity(intent)
                }
                else //if the information was incorrect, show an error message
                {
                    Toast.makeText(this@MainActivity,"Code incorect ",Toast.LENGTH_SHORT).show()
                }

            }

            //case when the request has failed to be sent
            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.i("Failure", t.message.toString())
            }

        })
    }
}