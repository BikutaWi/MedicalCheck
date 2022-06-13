package ch.hearc.medicalcheck

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.marginTop
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.hearc.medicalcheck.adapters.UserAdapter
import ch.hearc.medicalcheck.models.Follow
import ch.hearc.medicalcheck.models.User
import ch.hearc.medicalcheck.services.FollowService
import ch.hearc.medicalcheck.services.UsersService
import ch.hearc.medicalcheck.utils.Utils
import ch.hearc.medicalcheck.viewmodels.AddCareKeeperViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/*
* Project   : Medical Check Mobile
* Authors   : William Bikuta, Milán Cerviño, Ilyas Boillat, David Oktay
* Date      : 28.01.2022
* Class     : INF3dlm-a
* */

class AddCareKeeperActivity : AppCompatActivity() {

    private lateinit var customAdapter: UserAdapter
    private lateinit var viewModel: AddCareKeeperViewModel
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // hide action bar on activity
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        supportActionBar?.hide()

        setContentView(R.layout.activity_add_care_keeper)

        val buttonBack: Button = findViewById(R.id.btnBackAddCarekeeper)
        buttonBack.setOnClickListener(View.OnClickListener {
            finish()
        })

        viewModel = ViewModelProvider(this).get(AddCareKeeperViewModel::class.java)

        viewModel.getItems().observe(this, Observer {
            val recyclerView: RecyclerView = findViewById(R.id.careKeeperRecyclerView)
            customAdapter = UserAdapter(it)

            // on item click
            customAdapter.onItemClick = { user ->

                // load alert dialog
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Add carekeeper")
                builder.setMessage("input information")

                // create alert dialog inputs
                val layout = LinearLayout(this)
                val label1 = TextView(this)
                label1.text = "Relationshipname"
                val input = EditText(this)
                val label2 = TextView(this)
                label2.text = "\nCode"
                val code = EditText(this)
                code.inputType = InputType.TYPE_CLASS_NUMBER

                layout.orientation = LinearLayout.VERTICAL
                layout.addView(label1)
                layout.addView(input)
                layout.addView(label2)
                layout.addView(code)

                builder.setView(layout)

                // on "yes" click
                builder.setPositiveButton("Yes") { dialog, id ->

                    // get retrofit instance and create service
                    val retrofit = Utils.retrofitInstance
                    val service = retrofit?.create(UsersService::class.java)

                    // get user by code from Rest service
                    val userRequest = service?.getByCode(code.text.toString().toInt())

                    // enqueue query response
                    userRequest?.enqueue(object : Callback<User> {
                        override fun onResponse(call: Call<User>, response: Response<User>) {

                            // take user list from query response
                            val user = response.body()

                            // if user exists
                            if (user != null) {

                                // create relation
                                var follow: Follow = Follow()
                                follow.idusercarekeeper = user.id
                                follow.iduserpatient = Utils.userId
                                follow.relationship = input.text.toString()

                                //load update with viewmodel
                                viewModel.createFollow(follow)
                            } else {
                                dialog.dismiss()
                                Toast.makeText(this@AddCareKeeperActivity, "Code incorrect", Toast.LENGTH_LONG).show()
                            }
                        }

                        override fun onFailure(call: Call<User>, t: Throwable) {
                            //error ignored
                        }
                    })
                }
                builder.setNegativeButton("No") { dialog, id ->
                    // Dismiss the dialog
                    dialog.dismiss()
                }

                val alert = builder.create()
                alert.show()
            }

            val layoutManager = LinearLayoutManager(this@AddCareKeeperActivity?.applicationContext)
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = customAdapter
        })

        // load users list
        viewModel.loadUsers()

        // if follow has been created, close activity
        viewModel.getFollow().observe(this, Observer {
            if(it != null) {
                finish()
            }
        })

        searchView = findViewById(R.id.carekeeperSearchView)
        searchView.isSubmitButtonEnabled = false
        searchView.queryHint = "name ..."

        // update filter value
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(text: String?): Boolean {
                customAdapter.filter.filter(text)
                return true
            }

        })
    }
}