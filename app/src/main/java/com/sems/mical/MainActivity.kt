package com.sems.mical

import android.Manifest
import android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.*
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.*
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.single.BasePermissionListener
import com.sems.mical.data.AppDatabase
import android.net.Uri.fromParts
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.net.Uri
import android.provider.Settings


class MainActivity : AppCompatActivity() {

    private var mHandler: Handler? = null
    private var runnableCode : Runnable? = null
    private var mHandlerThread: HandlerThread? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        for(app in AppDatabase.getInstance(this)!!.appDao().getApps()){

        AppDatabase.getInstance(this)!!.appDao().deleteApp(app)
        }

        createNotificationChannel();
        checkRecordPermission()



        Dexter.withActivity(this)
            .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(BasePermissionListener()).check()
        Dexter.withActivity(this).withPermission(ACCESS_BACKGROUND_LOCATION).withListener(BasePermissionListener()).check()
    }

    private fun checkRecordPermission()
    {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                this,arrayOf( Manifest.permission.RECORD_AUDIO ),
                123
            );
        }
    }

    fun startAService(view: View){

        startForegroundService(Intent(this, MicMonitoringService::class.java))    }

    fun viewMicUsage(view:View){

        val intent = Intent(this,ViewUseActivity::class.java)
        startActivity(intent)
    }
    // Setup a recurring alarm every half hour
    fun scheduleAlarm() {
        this.mHandlerThread = HandlerThread("HandlerThread");
        mHandlerThread!!.start();
        this.mHandler = Handler(mHandlerThread!!.getLooper());


        runnableCode = object : Runnable {
            override fun run() {
                val i = Intent(applicationContext, MicMonitoringService::class.java)
                startForegroundService(i)
                // Repeat this the same runnable code block again another 2 seconds
                // 'this' is referencing the Runnable object
                mHandler!!.postDelayed(this, 6000)
            }
        }
        this.mHandler!!.postDelayed(runnableCode, 5000)
    }

    public fun stopService(view : View){


        stopService(Intent(this, MicMonitoringService::class.java))    }






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
