package com.sems.mical

import android.app.Notification.EXTRA_NOTIFICATION_ID
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import sensorapi.micapi.MicUsedImpl


class MainActivity : AppCompatActivity() {

    private var mHandler: Handler? = null

    private var mHandlerThread: HandlerThread? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        createNotificationChannel();

   }

    fun startAService(view: View){

        scheduleAlarm()
    }

    // Setup a recurring alarm every half hour
    fun scheduleAlarm() {
        this.mHandlerThread = HandlerThread("HandlerThread");
        mHandlerThread!!.start();
        this.mHandler = Handler(mHandlerThread!!.getLooper());


        val runnableCode = object : Runnable {
            override fun run() {
                val i = Intent(applicationContext, MicMonitoringService::class.java)
                startService(i)
                // Repeat this the same runnable code block again another 2 seconds
                // 'this' is referencing the Runnable object
                mHandler!!.postDelayed(this, 2000)
            }
        }
        this.mHandler!!.postDelayed(runnableCode, 1000)
    }

    public fun sendNotif(view : View){

    }






    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "hello";
            val descriptionText = "desc"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("hello", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}
