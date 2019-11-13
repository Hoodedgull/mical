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
    private var count = 38;
    override fun onReceive(p0: Context?, p1: Intent?) {
        Log.e("BBBB", "I AM RECEIVED")
        val action = p1?.getStringExtra("action")
        if (action == "accept") {
            acceptRequest(p0, p1)
            val toast = Toast.makeText(
                p0?.applicationContext,
                "This is a message displayed in an accepted Toast",
                Toast.LENGTH_SHORT
            )

            toast.show()

            Log.e("BBBB", "ACCEPTED APPPPPPPPPP")
        } else if (action == "decline") {
            declineRequest(p0, p1)
            val toast = Toast.makeText(
                p0?.applicationContext,
                "This is a message displayed in a declined Toast",
                Toast.LENGTH_SHORT
            )

            toast.show()

            Log.e("BBBB", "DECLINED!!! APPPPPPPPPP")
        }

        val apps = AppDatabase.getInstance(p0!!)!!.appDao().getApps();
        Log.e("HELP", apps.size.toString())
    }

    private fun acceptRequest(p0: Context?, p1: Intent?){
        val appName = p1!!.getStringExtra("appname")
        AppDatabase.getInstance(p0!!)!!.appDao().insertApp(App(appName, LocalDateTime.now().toString(), true, LocalDateTime.now().toString()))
    }

    private fun declineRequest(p0: Context?, p1: Intent?){
        val appName = p1!!.getStringExtra("appname")
        AppDatabase.getInstance(p0!!)!!.appDao().insertApp(App(appName, LocalDateTime.now().toString(), false, LocalDateTime.now().toString()))
    }


}