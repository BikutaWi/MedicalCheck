package ch.hearc.medicalcheck

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ch.hearc.medicalcheck.utils.AppNotificationService
import ch.hearc.medicalcheck.utils.Utils
import ch.hearc.medicalcheck.viewmodels.*
import java.util.*

/*
* Project   : Medical Check Mobile
* Authors   : William Bikuta, Milán Cerviño, Ilyas Boillat, David Oktay
* Date      : 28.01.2022
* Class     : INF3dlm-a
* */

class LoginActivity : AppCompatActivity() {

    lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // hide action bar on activity
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        supportActionBar?.hide()

        startService(Intent(this@LoginActivity, AppNotificationService::class.java))

        setContentView(R.layout.activity_login)

        // on sign up button click listener
        // start register register activity
        val buttonSignUp: Button = findViewById(R.id.btnSignUp)
        buttonSignUp.setOnClickListener(View.OnClickListener {
            val intentRegister = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intentRegister)
        })

        // on sign in button click listener
        // try to log in
        val buttonSignIn: Button = findViewById(R.id.btnSignIn)
        buttonSignIn.setOnClickListener(View.OnClickListener {
            login()
        })

        if(Utils.userId != 0) {
            val intentMain = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intentMain)
        }
    }

    /***
     * login accept
     */
    fun accept(id: Int) {
        Utils.userId = id

        val intentMain = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intentMain)
    }

    /***
     * show status message
     */
    fun showMessage(message: String) {
        Toast.makeText(this@LoginActivity, message, Toast.LENGTH_LONG).show()
    }

    /***
     * check input and try to log in
     */
    fun login() {
        val tbxUserName: TextView = findViewById(R.id.editUsername)
        val tbxPassword: TextView = findViewById(R.id.editPassword)

        // if inputs are empty
        if(tbxUserName.text.isEmpty() || tbxPassword.text.isEmpty())
        {
            showMessage("Please fill all the fields")
        }
        else
        {
            val username: String = tbxUserName.text.toString()
            val password: String = tbxPassword.text.toString()

            // create viewmodel
            viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

            // create state observer
            viewModel.getState().observe(this, Observer { updateUi(it!!) })

            // call viewmodel login function
            viewModel.login(username, password)
        }
    }

    /***
     * update ui with state message
     */
    private fun updateUi(state: LoginViewModelState) {
        return when (state) {
            is LoginViewModelStateSuccess -> {
                showMessage(state.message)
                accept(state.userid)
            }
            is LoginViewModelStateFailure -> {
                showMessage(state.message)
            }
            else -> {
                showMessage("Fatal error !")
            }
        }
    }
}