package ch.hearc.medicalcheck.ui.home

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.hearc.medicalcheck.MainActivity
import ch.hearc.medicalcheck.R
import ch.hearc.medicalcheck.databinding.FragmentHomeBinding
import ch.hearc.medicalcheck.models.Treatment
import ch.hearc.medicalcheck.models.User
import ch.hearc.medicalcheck.services.UsersService
import ch.hearc.medicalcheck.ui.medecine.MedicineAdapter
import ch.hearc.medicalcheck.utils.Utils
import ch.hearc.medicalcheck.utils.Utils.Companion.md5
import ch.hearc.medicalcheck.viewmodels.LoginViewModelStateFailure
import ch.hearc.medicalcheck.viewmodels.LoginViewModelStateSuccess
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/*
* Project   : Medical Check Mobile
* Authors   : William Bikuta, Milán Cerviño, Ilyas Boillat, David Oktay
* Date      : 28.01.2022
* Class     : INF3dlm-a
* */

class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var customAdapter: HomeAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        // create viewmodel
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        val view:View = inflater.inflate(R.layout.fragment_home, container, false)
        val tbxHello: TextView = view.findViewById(R.id.txtHello)
        val tbxLastBeat: TextView = view.findViewById(R.id.txtLastBeat)
        val tbxDate: TextView = view.findViewById(R.id.txtDate)

        // create observer for user's firstname
        // when firstname change update textview
        viewModel.getFirstname().observe(viewLifecycleOwner, Observer { tbxHello.text = "Hello $it" })

        // call load user viewmodel function
        viewModel.loadUser()

        viewModel.getTreatment().observe(viewLifecycleOwner, Observer {
            val recyclerView: RecyclerView = view.findViewById(R.id.homeRecyclerView)
            customAdapter = HomeAdapter(it as ArrayList<Treatment>)

            // on item click
            customAdapter.onItemClick = { treatment ->

                // load aler dialog
                val builder = AlertDialog.Builder(activity)
                builder.setMessage("Are you sure your treatment has been taken ?\n\nReminder will disappear")
                    .setCancelable(false)

                        // on "yes" click
                    .setPositiveButton("Yes") { dialog, id ->
                        treatment.istaken = true

                        // medicine is taken, remove from list and update database
                        viewModel.takeTreatment(treatment)
                        customAdapter.removeItem(treatment)
                        customAdapter.notifyDataSetChanged()
                    }
                        // on "no" click
                    .setNegativeButton("No") { dialog, id ->
                        // Dismiss the dialog
                        dialog.dismiss()
                    }
                val alert = builder.create()
                alert.show()
            }

            val layoutManager = LinearLayoutManager(activity?.applicationContext)
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = customAdapter
        })

        // load recyclerview items
        viewModel.loadTreatments()

        // if view receive measure, show it on view
        viewModel.getMeasure().observe(viewLifecycleOwner, Observer {
            tbxLastBeat.text = it.heartrate.toString() + " bpm"

            val date = it.date
            date!!.hours = date.hours-1

            tbxDate.text = date.toString().dropLast(7)
        })

        // load measure information
        viewModel.loadMeasure()

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}