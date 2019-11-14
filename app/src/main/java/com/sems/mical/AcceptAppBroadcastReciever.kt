package com.sems.mical

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.View
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.getSystemService
import android.widget.Toast
import com.sems.mical.data.AppDatabase
import com.sems.mical.data.entities.App
import com.sems.mical.data.entities.MicrophoneIsBeingUsed
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class AcceptAppBroadcastReciever : BroadcastReceiver() {

    override fun onReceive(p0: Context?, p1: Intent?) {
        Log.e("BBBB", "I AM RECEIVED")
        val action = p1?.getStringExtra("action")
        val appName = p1?.getStringExtra("appname")!!
        if (action == "accept") {

            handleRequest(p0, appName, true)
            Toast.makeText(
                p0?.applicationContext,
                "${appName} has been allowed access to the mic",
                Toast.LENGTH_SHORT
            ).show()

            Log.e("BBBB", "ACCEPTED APPPPPPPPPP")
        } else if (action == "decline") {
            handleRequest(p0, appName, false)
            Toast.makeText(
                p0?.applicationContext,
                "${appName} has not been allowed access to the mic",
                Toast.LENGTH_SHORT
            ).show()

            Log.e("BBBB", "DECLINED!!! APPPPPPPPPP")
        }

        with(NotificationManagerCompat.from(p0!!)) {
            // notificationId is a unique int for each notification that you must define
            cancel(123456789)
        }

        val apps = AppDatabase.getInstance(p0!!)!!.appDao().getApps();
        Log.e("HELP", apps.size.toString())
    }

    private fun handleRequest(p0: Context?, appName: String, permission: Boolean){
        Log.e("BBBB", "I AM DB")
        var dbInstance = AppDatabase.getInstance(p0!!)!!.appDao()
        if (dbInstance.getAppByName(appName).isEmpty() ){
            dbInstance.insertApp(App(appName, appName, permission, LocalDateTime.now().toString()))
        }
    }


}