package ch.hearc.medicalcheck.ui.carekeeper

import android.app.AlertDialog
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.hearc.medicalcheck.AddCareKeeperActivity
import ch.hearc.medicalcheck.AddMedicineActivity
import ch.hearc.medicalcheck.R
import ch.hearc.medicalcheck.models.Follow
import ch.hearc.medicalcheck.models.Treatment
import ch.hearc.medicalcheck.ui.home.HomeAdapter

/*
* Project   : Medical Check Mobile
* Authors   : William Bikuta, Milán Cerviño, Ilyas Boillat, David Oktay
* Date      : 28.01.2022
* Class     : INF3dlm-a
* */

class CareKeeperFragment : Fragment() {

    private lateinit var viewModel: CareKeeperViewModel
    private lateinit var customAdapter: CareKeeperAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View = inflater.inflate(R.layout.fragment_care_keeper, container, false)

        // "Add" button listener
        val buttonAdd: Button = view.findViewById(R.id.btnAddUser)
        buttonAdd.setOnClickListener(View.OnClickListener {
            val intent = Intent(activity, AddCareKeeperActivity::class.java)
            startActivity(intent)
        })

        viewModel = CareKeeperViewModel()

        // when view get items, create an adapter
        viewModel.getItems().observe(viewLifecycleOwner, Observer {
            val recyclerView: RecyclerView = view.findViewById(R.id.patientRecyclerView)
            customAdapter = CareKeeperAdapter(it as ArrayList<Follow>, requireActivity())

            val layoutManager = LinearLayoutManager(activity?.applicationContext)
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = customAdapter
        })

        // load recycler view items
        viewModel.loadFollows()

        return view
    }

}