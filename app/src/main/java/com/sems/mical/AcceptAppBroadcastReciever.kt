package com.sems.mical

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import android.util.Log
import android.view.View
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.getSystemService
import android.widget.Toast
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.LocationServices
import com.sems.mical.MicMonitoringService.Companion.context
import com.sems.mical.data.AppDatabase
import com.sems.mical.data.entities.App
import com.sems.mical.data.entities.MicrophoneIsBeingUsed
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.Result.Companion.failure
import kotlin.Result.Companion.success


class AcceptAppBroadcastReciever : BroadcastReceiver() {

    override fun onReceive(p0: Context?, p1: Intent?) {
        Log.e("BBBB", "I AM RECEIVED")
        val action = p1?.getStringExtra("action")
        val appName = p1?.getStringExtra("appname")!!
        val latitude = p1?.getDoubleExtra("latitude", -1.0)
        val longitude = p1?.getDoubleExtra("longitude", -1.0)


        val geofencingClient = LocationServices.getGeofencingClient(context)


        if (action == "accept") {

            handleRequest(p0, appName, true,latitude, longitude)
            Toast.makeText(
                p0?.applicationContext,
                "${appName} has been allowed access to the mic",
                Toast.LENGTH_SHORT
            ).show()

            val fence = buildGeofence(appName,latitude,longitude,500.0f)
            // TODO IS NOT WORKS YET
            // https://www.raywenderlich.com/7372-geofencing-api-tutorial-for-android#toc-anchor-005
            geofencingClient
                .addGeofences(buildGeofencingRequest(fence), geofencePendingIntent)
                // 3
                .addOnSuccessListener {
                    saveAll(getAll() + reminder)
                    success()
                }
                // 4
                .addOnFailureListener {
                    failure("Error")
                }
            Log.e("BBBB", "ACCEPTED APPPPPPPPPP")
        } else if (action == "decline") {
            handleRequest(p0, appName, false, latitude, longitude)
            Toast.makeText(
                p0?.applicationContext,
                "${appName} has not been allowed access to the mic",
                Toast.LENGTH_SHORT
            ).show()

            Log.e("BBBB", "DECLINED!!! APPPPPPPPPP")
        }

        var id = p1.getIntExtra(Notification.EXTRA_NOTIFICATION_ID, -1)
        with(NotificationManagerCompat.from(p0!!)) {
            // notificationId is a unique int for each notification that you must define
            cancel(id)
        }

        val apps = AppDatabase.getInstance(p0!!)!!.appDao().getApps();
        Log.e("HELP", apps.size.toString())
    }

    private fun handleRequest(p0: Context?, appName: String, permission: Boolean, latitude: Double, longitude: Double){
        Log.e("BBBB", "Location: ${latitude}")
        var dbInstance = AppDatabase.getInstance(p0!!)!!.appDao()
        if (dbInstance.getAppByName(appName).isEmpty() ){
            dbInstance.insertApp(App(appName, appName, permission, LocalDateTime.now().toString(), latitude, longitude))
        }
    }

    private fun buildGeofence(appName:String,lat:Double, long:Double, radius: Float): Geofence? {
        val latitude = lat
        val longitude = long


        if (latitude != null && longitude != null && radius != null) {
            return Geofence.Builder()
                // 1
                .setRequestId(appName)
                // 2
                .setCircularRegion(
                    latitude,
                    longitude,
                    radius
                )
                // 3
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_EXIT)
                // 4
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .build()
        }

        return null
    }


}