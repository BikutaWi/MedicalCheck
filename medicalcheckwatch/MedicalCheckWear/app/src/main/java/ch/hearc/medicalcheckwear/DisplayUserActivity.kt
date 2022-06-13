package ch.hearc.medicalcheckwear

import android.app.Activity
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import ch.hearc.medicalcheckwear.databinding.ActivityDisplayUserBinding
import ch.hearc.medicalcheckwear.models.Notification
import ch.hearc.medicalcheckwear.models.NotificationType
import ch.hearc.medicalcheckwear.services.NotificationService
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.sql.Timestamp
import java.time.format.DateTimeFormatter.*
import android.hardware.SensorManager
import android.os.Handler
import android.os.Looper
import ch.hearc.medicalcheckwear.models.Measure
import ch.hearc.medicalcheckwear.services.MeasureService
import com.google.android.gms.location.LocationRequest
import java.util.*
import com.google.android.gms.tasks.CancellationTokenSource

/*
* Project   : Medical Check Watch
* Authors   : William Bikuta, Milán Cerviño, Ilyas Boillat, David Oktay
* Date      : 28.01.2022
* Class     : INF3dlm-a
* */

/**
 * view which allow to recolt heart information
 * the user can also ask for help when clicking on a button,
 * which generate a notification containing the location
 * and alert each carekeeper
 */
class DisplayUserActivity : Activity() {

    private lateinit var binding: ActivityDisplayUserBinding

    //api google to manage location
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDisplayUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        //get the id which has been put by the mainActivity
        val idUser = intent.getIntExtra(USER,-1)

        //when the user click on the button send a notification
        findViewById<Button>(R.id.btnHelp).setOnClickListener{
            fetchLocation(idUser) //fetch location and send the notification
        }

        //every 30s recolt a heart rate and store it in the database, through the REST service
        val timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                measure(idUser) //measure for the user his heart rate
            }
        }, 0, 30000)


    }

    /*
    * send a notification of type help with the current location
    */
    private fun sendNotification(idUser: Int, latitude: Double, longitude: Double) {

        //set the date format
        val gson : Gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create()

        //build the rest request
        val retro = Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        //request which is associate to the notificationService
        val service = retro.create(NotificationService::class.java)

        //build a notification
        val notification = Notification()
        notification.iduser = idUser //user which has require help
        //location
        notification.latitude = latitude //location
        notification.longitude = longitude

        notification.notificationtype = NotificationType.HELP //asking help
        notification.isclosed = false //indicate that this notification has not been treated
        notification.date = Timestamp(System.currentTimeMillis()) //now

        //indicate we want to create a notification with our request
        val userRequest = service.create(notification)

        //enqueue the request, wait async to get an answer
        userRequest.enqueue(object : Callback<Notification> {
            override fun onResponse(call: Call<Notification>, response: Response<Notification>) {

                //when we get an anwser
                val notification = response.body()

                //if the notification was created sucessfully
                if(notification != null)
                {
                    //inform the user
                    Toast.makeText(this@DisplayUserActivity,"Request sent ",Toast.LENGTH_SHORT).show()
                }
                else //if an error has been encouter when we try to create the notification
                {
                    Toast.makeText(this@DisplayUserActivity,"Error encounter",Toast.LENGTH_SHORT).show()
                }

            }

            //when the request has not been sent
            override fun onFailure(call: Call<Notification>, t: Throwable) {
                Log.i("Failure", t.message.toString())
            }

        })
    }

    /**
     * send a measure for the current user
     */
    private fun sendMeasure(idUser: Int, rate: Int) {

        //set the date format
        val gson : Gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create()

        //build the rest request
        val retro = Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        //request which is associate to the notificationService
        val service = retro.create(MeasureService::class.java)

        //build a measure
        val measure = Measure()
        measure.iduser = idUser //current user
        measure.heartrate = rate //heart rate
        measure.date = Timestamp(System.currentTimeMillis()) //now


        //indicate we want to create a measure with our request
        val measureRequest = service.create(measure)

        //enqueue the request, wait async to get an answer
        measureRequest.enqueue(object : Callback<Measure> {

            //when we get an anwser
            override fun onResponse(call: Call<Measure>, response: Response<Measure>) {
               //nothting
            }

            //when the request has not been sent
            override fun onFailure(call: Call<Measure>, t: Throwable) {
                //nothing
            }

        })
    }

    /*
    * get the current location
    */
    private fun fetchLocation(idUser: Int) {

        //check if the right was granted, else ask for them
        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat
                .checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),101)
            return
        }

        //by default, if the location has never been asked, the lastLocation is null
        //so we ask for the current location
        var cancellationTokenSource = CancellationTokenSource()
        fusedLocationProviderClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY,
            cancellationTokenSource.token)

        //get the lastLocation
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            //if we have a location
            if(location != null){
                //send a notification with the current location
                sendNotification(idUser,location.latitude,location.longitude)
            }
        }
    }

    /*
    * allow to record a measure
     */
    private  fun measure(idUser: Int)
    {
        //check if the right was granted, else ask for them
        if (ActivityCompat.checkSelfPermission(this,android.Manifest.permission.BODY_SENSORS)
            != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.BODY_SENSORS),102)
        }

        //get the sensor for the heareRate
        val mSensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        val mHeartRateSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE)

        //register to listen when the heart rate has changed
        mSensorManager.registerListener(object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                //if it's the heart_rate sensor which has trigger the event
                if (event!!.sensor.type === Sensor.TYPE_HEART_RATE) {
                    val rate = event!!.values[0].toInt() //get the rate
                    if(rate != 0) //if it's not null
                    {
                        Log.d("onSensorChanged", rate.toString())
                        sendMeasure(idUser,rate) //send the information to the database

                        //unregister to avoid spamming the hear rate
                        mSensorManager.unregisterListener(this)
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                //nothing
            }

        }, mHeartRateSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

}