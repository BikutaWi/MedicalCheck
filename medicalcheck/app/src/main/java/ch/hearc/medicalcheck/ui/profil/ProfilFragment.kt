package ch.hearc.medicalcheck.ui.profil

import android.app.Service
import android.content.Intent
import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.CountDownTimer
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import ch.hearc.medicalcheck.*
import ch.hearc.medicalcheck.utils.AppNotificationService
import ch.hearc.medicalcheck.utils.Utils

/*
* Project   : Medical Check Mobile
* Authors   : William Bikuta, Milán Cerviño, Ilyas Boillat, David Oktay
* Date      : 28.01.2022
* Class     : INF3dlm-a
* */

class ProfilFragment : Fragment() {

    private lateinit var viewModel: ProfilViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        // create viewmodel
        viewModel = ViewModelProvider(this).get(ProfilViewModel::class.java)

        val view:View = inflater.inflate(R.layout.fragment_profil, container, false)

        val tbxProfilName: TextView = view.findViewById(R.id.txtProfilUsername)
        val tbxProfilEmail: TextView = view.findViewById(R.id.txtProfilEmail)
        val tbxCode: TextView = view.findViewById(R.id.txtCode)
        val progressBarAvailableTimeLeft: ProgressBar = view.findViewById(R.id.progressBarAvailableTimeLeft)

        val btnUpdate: Button = view.findViewById(R.id.btnUpdate)
        val btnGenerateCode: Button = view.findViewById(R.id.btnGenerateCode)
        val btnLogout: Button = view.findViewById(R.id.btnLogout)
        val btnGraph: Button = view.findViewById(R.id.btnGraph)

        var isRunning = false

        // progress bar timer
        val timer = object: CountDownTimer(30000, 1000) {

            // ont tick decrease progress bar value
            override fun onTick(millisUntilFinished: Long) {
                progressBarAvailableTimeLeft.progress -= 1
                isRunning = true
            }
            override fun onFinish() {
                tbxCode.text = ""
                progressBarAvailableTimeLeft.visibility = View.GONE
                tbxCode.visibility = View.GONE
                isRunning = false

            }
        }

        // create observer to change user names and email when mutablelivedata change
        viewModel.getNames().observe(viewLifecycleOwner, Observer { tbxProfilName.text = it })
        viewModel.getEmail().observe(viewLifecycleOwner, Observer { tbxProfilEmail.text = it })
        viewModel.getCode().observe(viewLifecycleOwner, Observer { tbxCode.text = it })


        // call viewmodel load user function
        viewModel.loadUser()

        // "Update" button click listener
        btnUpdate.setOnClickListener(View.OnClickListener {
            val intent = Intent(activity, UpdateProfilActivity::class.java)

            // start update profil activity and wait for result
            startActivityForResult(intent, 1)
        })

        // "Graph" button click listener
        btnGraph.setOnClickListener(View.OnClickListener {
            val intent = Intent(activity, GraphActivity::class.java)
            startActivity(intent)
        })

        // genereate code on click
        btnGenerateCode.setOnClickListener(View.OnClickListener { view ->

            // load code
            viewModel.loadCode()

            // show progress bar
            progressBarAvailableTimeLeft.visibility = View.VISIBLE
            tbxCode.visibility = View.VISIBLE
            progressBarAvailableTimeLeft.progress = 30


            if(isRunning)
                timer.cancel()

            timer.start()
        })

        // on logout click
        btnLogout.setOnClickListener(View.OnClickListener { view ->
            Utils.logout = true
            Utils.userId = 0

            // restart notification service
            activity?.startService(Intent(activity, AppNotificationService::class.java))

            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
        })

        return view
    }


    /***
     * when activity get result
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        viewModel.loadUser()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }
}