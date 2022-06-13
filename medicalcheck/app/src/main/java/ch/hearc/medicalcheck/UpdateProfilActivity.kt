package ch.hearc.medicalcheck

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
import ch.hearc.medicalcheck.models.User
import ch.hearc.medicalcheck.utils.Utils.Companion.md5
import ch.hearc.medicalcheck.viewmodels.*

/*
* Project   : Medical Check Mobile
* Authors   : William Bikuta, Milán Cerviño, Ilyas Boillat, David Oktay
* Date      : 28.01.2022
* Class     : INF3dlm-a
* */

class UpdateProfilActivity : AppCompatActivity() {

    lateinit var viewModel: UpdateProfilViewModel
    lateinit var user: User

    lateinit var tbxUserName: TextView
    lateinit var tbxPassword: TextView
    lateinit var tbxConfirm: TextView
    lateinit var tbxEmail: TextView
    lateinit var tbxLastname: TextView
    lateinit var tbxFirstname: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // hide action bar on activity
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        supportActionBar?.hide()

        setContentView(R.layout.activity_update_profil)

        // on back button listener
        // finish activity
        val btnBack: Button = findViewById(R.id.btnBackUpdateProfil)
        btnBack.setOnClickListener(View.OnClickListener {
            finish()
        })

        tbxUserName = findViewById(R.id.editUpdateUsername)
        tbxPassword = findViewById(R.id.editUpdatePassword)
        tbxConfirm = findViewById(R.id.editUpdateConfirm)
        tbxEmail = findViewById(R.id.editUpdateMail)
        tbxFirstname = findViewById(R.id.editUpdateFirstname)
        tbxLastname = findViewById(R.id.editUpdateLastName)

        // create viewmodel
        viewModel = ViewModelProvider(this).get(UpdateProfilViewModel::class.java)

        // create observer to update ui with state message
        viewModel.getState().observe(this, Observer { getMessage(it!!) })
        // create observer to update user informations
        viewModel.getUser().observe(this, Observer { updateUI(it!!) })

        // call viewmodel load user function
        viewModel.loadUser()

        // on save button click listener
        // try to save data into database
        val btnSave: Button = findViewById(R.id.btnSave)
        btnSave.setOnClickListener(View.OnClickListener {
            trySave()
        })
    }

    /***
     * show status message in ui
     */
    fun showMessage(message: String) {
        Toast.makeText(this@UpdateProfilActivity, message, Toast.LENGTH_LONG).show()
    }

    /***
     * try to save user information
     */
    private fun trySave() {
        user.username = tbxUserName.text.toString()
        user.firstname = tbxFirstname.text.toString()
        user.lastname = tbxLastname.text.toString()
        user.email = tbxEmail.text.toString()

        // if password and confirm are not empty and match
        if(tbxPassword.text.isNotEmpty() or tbxConfirm.text.isNotEmpty())
        {
            if(!tbxPassword.text.toString().equals(tbxConfirm.text.toString()))
            {
                showMessage("Error: Password and confirmation do not match")
                return
            }
            else
            {
                // hash new password
                user.password = tbxPassword.text.toString().md5()
            }
        }

        // save user
        viewModel.save(user)
    }

    /***
     * show state message
     */
    private fun getMessage(state: UpdateProfilViewModelState) {
        return when (state) {
            is UpdateProfilViewModelStateSuccess -> {
                showMessage(state.message)
                finish()
            }
            is UpdateProfilViewModelStateFailure -> {
                showMessage(state.message)
            }
            else -> {
                showMessage("Fatal error !")
            }
        }
    }

    /***
     * update inputs information
     */
    private fun updateUI(userData: User) {
        user = userData

        tbxFirstname.text = user.firstname
        tbxLastname.text = user.lastname
        tbxUserName.text = user.username
        tbxEmail.text = user.email
    }
}