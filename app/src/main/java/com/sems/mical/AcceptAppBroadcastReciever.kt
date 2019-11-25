package com.sems.mical

import android.app.Notification
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import android.widget.Toast
import com.google.android.gms.maps.model.LatLng
import com.sems.mical.data.AppDatabase
import com.sems.mical.data.entities.App
import java.time.LocalDateTime


class AcceptAppBroadcastReciever : BroadcastReceiver() {

    override fun onReceive(p0: Context?, p1: Intent?) {
        Log.e("BBBB", "I AM RECEIVED")
        val action = p1?.getStringExtra("action")
        val appName = p1?.getStringExtra("appname")!!
        val latitude = p1?.getDoubleExtra("latitude", -1.0)
        val longitude = p1?.getDoubleExtra("longitude", -1.0)


        if (action == "accept") {

            handleRequest(p0, appName, true, latitude, longitude)
            Toast.makeText(
                p0?.applicationContext,
                "${appName} has been allowed access to the mic",
                Toast.LENGTH_SHORT
            ).show()

            val reminder = Reminder(getUniqueId().toString(), LatLng(latitude,longitude),200.0,"TEST")
            (p0?.applicationContext as MicalApp).getRepository().add(reminder,null, null);


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

    private fun handleRequest(
        p0: Context?,
        appName: String,
        permission: Boolean,
        latitude: Double,
        longitude: Double
    ) {
        Log.e("BBBB", "Location: ${latitude}")
        var dbInstance = AppDatabase.getInstance(p0!!)!!.appDao()
        if (dbInstance.getAppByName(appName).isEmpty()) {
            dbInstance.insertApp(
                App(
                    appName,
                    appName,
                    permission,
                    LocalDateTime.now().toString(),
                    latitude,
                    longitude
                )
            )
        }
    }
}

