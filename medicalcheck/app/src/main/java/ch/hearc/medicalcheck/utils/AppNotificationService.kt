package ch.hearc.medicalcheck.utils

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import java.util.*
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import ch.hearc.medicalcheck.R
import ch.hearc.medicalcheck.models.Notification
import ch.hearc.medicalcheck.models.NotificationType
import ch.hearc.medicalcheck.models.User
import ch.hearc.medicalcheck.services.NotificationService
import ch.hearc.medicalcheck.services.UsersService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/*
* Project   : Medical Check Mobile
* Authors   : William Bikuta, Milán Cerviño, Ilyas Boillat, David Oktay
* Date      : 28.01.2022
* Class     : INF3dlm-a
* */

// source : https://www.tutorialspoint.com/send-a-notification-when-the-android-app-is-closed

class AppNotificationService : Service() {

    val MY_PREFS_NAME = "MedicalCheck"

    var timer: Timer? = null
    lateinit var timerTask: TimerTask
    var userid: Int = 0

    override fun onBind(p0: Intent?): IBinder? {
        Log.i("Service", "bind")
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i("Service", "start")
        super.onStartCommand(intent, flags, startId)

        // if user just logout
        if(Utils.logout) {

            // update userid to null value
            userid = Utils.userId
            var editor: SharedPreferences.Editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit()
            editor.putInt("userid", userid)
            editor.apply()

            // change flag value
            Utils.logout = false
        }

        // if service has already user id
        if(Utils.userId != 0) {

            // try to get it from static vars, and update preferences
            userid = Utils.userId
            var editor: SharedPreferences.Editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit()
            editor.putInt("userid", userid)
            editor.apply()
        }

        // if there's no user id
        else {

            // try to get it form preferences to get last connected user
            var prefs: SharedPreferences = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE)
            userid = prefs.getInt("userid", 0)

            if(userid != 0) {
                Utils.userId = userid
            }
        }

        startTimer()
        return START_STICKY
    }


    override fun onCreate() {
        Log.i("Service", "create")
    }

    override fun onDestroy() {
        Log.i("Service", "destroy")
        stoptimertask()
        super.onDestroy()
    }

    //we are going to use a handler to be able to run in our TimerTask
    val handler: Handler = Handler()

    private fun startTimer() {
        //set a new Timer
        timer = Timer()

        //initialize the TimerTask's job
        initializeTimerTask()

        //schedule the timer, after the first 5000ms the TimerTask will run every 1000ms
        timer!!.schedule(timerTask, 5000, 1000) //
    }

    private fun stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer!!.cancel()
            timer = null
        }
    }

    fun initializeTimerTask() {
        timerTask = object : TimerTask() {

            override fun run() {

                handler.post(Runnable {
                    // load notification on timer tick
                    loadNotifications()
                })
            }
        }
    }

    /**
     * load notification if exist in database
     */
    private fun loadNotifications() {
        // get retrofit instance and create service
        val retrofit = Utils.retrofitInstance
        val service = retrofit?.create(NotificationService::class.java)

        // get notification, that service has not already send from Rest service
        val notifRequest = service?.getAllByIdUser(userid)

        // enqueue request response
        notifRequest?.enqueue(object : Callback<List<Notification>> {
            override fun onResponse(call: Call<List<Notification>>, response: Response<List<Notification>>) {

                // get notifications
                val list = response.body()

                // if there's notification
                if (list != null) {
                    createNotification(list)
                }
            }

            override fun onFailure(call: Call<List<Notification>>, t: Throwable) {
                error("fail to load notifications")
            }
        })
    }

    /**
     * create notification to show on phone
     */
    private fun createNotification(list: List<Notification>) {
        for (notification in list) {

            val retrofit = Utils.retrofitInstance
            val service = retrofit?.create(UsersService::class.java)

            // get user information from Rest service
            val userRequest = notification.iduser?.let { service?.get(it) }

            // enqueue query response
            userRequest?.enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {

                    // take user model from query response
                    val user = response.body()

                    // if user exist
                    if (user != null)
                    {
                        // show notification
                        showNotification(notification, user)

                        // update notification status
                        updateNotification(notification)
                    }
                    else
                    {
                        error("user doesn't exist")
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    error("user doesn't exist")
                }
            })
        }
    }

    /**
     * Put notification is closed to avoid resending it every time
     */
    private fun updateNotification(notification: Notification) {
        notification.isclosed = true

        // get retrofit instance and create service
        val retrofit = Utils.retrofitInstance
        val service = retrofit?.create(NotificationService::class.java)

        // update notif in Rest service
        val notifRequest = service?.update(notification.id!!, notification)

        // enqueue request response
        notifRequest?.enqueue(object : Callback<Notification> {
            override fun onResponse(call: Call<Notification>, response: Response<Notification>) {

                // get notification
                val notif = response.body()
            }

            override fun onFailure(call: Call<Notification>, t: Throwable) {
                error("fail to update notification")
            }
        })
    }

    /**
     * Show notification on screen
     */
    private fun showNotification(notification: Notification, user: User) {
        val mNotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val mBuilder: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, "default")

        var contentText = ""

        // test notification type
        when (notification.notificationtype) {

            // if it's help notification
            NotificationType.HELP ->
            {
                if(notification.iduser != Utils.userId)
                    contentText = user.firstname + " " + user.lastname + " asked for help"
                else
                    contentText = "You asked for help"
                mBuilder.setContentTitle("Help required")
                mBuilder.setContentText(contentText)
                mBuilder.setTicker(contentText)

                val latitude = notification.latitude.toString()
                val longitude = notification.longitude.toString()

                // open google map on click with user coordinates
                val geoUri = "http://maps.google.com/maps?q=loc:" + latitude + "," + longitude + "(" + contentText + ")"
                val gmpUri = Uri.parse(geoUri)
                val intent = Intent(Intent.ACTION_VIEW, gmpUri)
                intent.setPackage("com.google.android.apps.maps")
                val contentIntent = PendingIntent.getActivity(this, 0, intent, 0)
                mBuilder.setContentIntent(contentIntent)
            }

            // if it's treatment notification
            NotificationType.TREATMENT_NOT_TAKEN ->
            {
                // if it is different user (if it's patient user)
                if(notification.iduser != Utils.userId)
                    contentText = user.firstname + " " + user.lastname + " has not taken his treatment"
                else
                    contentText = "Don't forget to take your treatment"
                mBuilder.setContentTitle("Treatment not taken")
                mBuilder.setContentText(contentText)
                mBuilder.setTicker(contentText)
            }
        }

        mBuilder.setSmallIcon(R.mipmap.ic_icon)
        mBuilder.setAutoCancel(true)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel("1", "NOTIFICATION_CHANNEL_NAME", importance)
            mBuilder.setChannelId("1")
            assert(mNotificationManager != null)
            mNotificationManager.createNotificationChannel(notificationChannel)
        }

        assert(mNotificationManager != null)
        mNotificationManager.notify(System.currentTimeMillis().toInt(), mBuilder.build())
    }
}