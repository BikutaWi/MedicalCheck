package ch.hearc.medicalcheck

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ch.hearc.medicalcheck.models.User
import ch.hearc.medicalcheck.utils.Utils
import ch.hearc.medicalcheck.viewmodels.*

/*
* Project   : Medical Check Mobile
* Authors   : William Bikuta, Milán Cerviño, Ilyas Boillat, David Oktay
* Date      : 28.01.2022
* Class     : INF3dlm-a
* */

class RegisterActivity : AppCompatActivity() {

    lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // hide action bar on activity
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        supportActionBar?.hide()

        setContentView(R.layout.activity_register)

        // on register button click listener
        // try to register new user
        val buttonRegister: Button = findViewById(R.id.btnRegister)
        buttonRegister.setOnClickListener(View.OnClickListener {
            register()
        })

        // on back button click listener
        // finish
        val buttonBack: Button = findViewById(R.id.btnBackRegister)
        buttonBack.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    /***
     * resgister accept
     */
    fun accept(id: Int) {
        Utils.userId = id

        val intentMain = Intent(this@RegisterActivity, MainActivity::class.java)
        startActivity(intentMain)
    }

    /***
     * show status message
     */
    fun showMessage(message: String) {
        Toast.makeText(this@RegisterActivity, message, Toast.LENGTH_LONG).show()
    }

    /***
     * resgister new user
     */
    fun register() {
        val tbxUserName: TextView = findViewById(R.id.editNewUsername)
        val tbxPassword: TextView = findViewById(R.id.editNewPassword)
        val tbxConfirm: TextView = findViewById(R.id.editConfirm)
        val tbxEmail: TextView = findViewById(R.id.editEmail)
        val tbxFirstname: TextView = findViewById(R.id.editFirstname)
        val tbxLastname: TextView = findViewById(R.id.editLastname)
        val cbxIsCarekeeper: CheckBox = findViewById(R.id.cbxIsCareKeeper)

        // check required inputs
        if(tbxUserName.text.toString().isEmpty() ||
            tbxEmail.text.toString().isEmpty() ||
            tbxPassword.text.toString().isEmpty() ||
            tbxFirstname.text.toString().isEmpty() ||
            tbxLastname.text.toString().isEmpty() ||
            tbxConfirm.text.toString().isEmpty())
        {
            showMessage("Please fill all the fields")
        }
        else
        {
            val username: String = tbxUserName.text.toString()
            val password: String = tbxPassword.text.toString()
            val firstname: String = tbxFirstname.text.toString()
            val lastname: String = tbxLastname.text.toString()
            val email: String = tbxEmail.text.toString()
            val confirm: String = tbxConfirm.text.toString()
            val isCarekeeper: Boolean = cbxIsCarekeeper.isChecked()

            // create viewmodel
            viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

            // create observer to update ui on viewmodel state
            viewModel.getState().observe(this, Observer { updateUi(it!!) })

            // call viewmodel register function
            viewModel.register(firstname, lastname, email, confirm, username, password, isCarekeeper)
        }
    }

    /***
     * Update ui with state message
     */
    private fun updateUi(state: RegisterViewModelState) {
        return when (state) {
            is RegisterViewModelStateSuccess -> {
                showMessage(state.message)
                accept(state.userid)
            }
            is RegisterViewModelStateFailure -> {
                showMessage(state.message)
            }
            else -> {
                showMessage("Fatal error !")
            }
        }
    }
}