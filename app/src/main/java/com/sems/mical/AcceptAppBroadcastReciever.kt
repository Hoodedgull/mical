package com.sems.mical

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.View
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.getSystemService



class AcceptAppBroadcastReciever : BroadcastReceiver() {
    private var count = 38;
    override fun onReceive(p0: Context?, p1: Intent?) {




        sendNotif(p0);

    }

    public fun sendNotif(p0: Context?){

        if( p0 is Context){
        createNotificationChannel(p0)
        var builder = NotificationCompat.Builder(p0, "hello")
            .setSmallIcon(R.drawable.ic_stat_onesignal_default)
            .setContentTitle("APP ACCEPTED")
            .setContentText("moretext")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(p0)) {
            // notificationId is a unique int for each notification that you must define
            notify(count++, builder.build())
        }

        } else {
            throw Exception("help");
        }
    }

    private fun createNotificationChannel(p0:Context) {


        val notificationManager =
            p0.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        var channel = NotificationChannel(
            "com.myApp",
            "My App",
            NotificationManager.IMPORTANCE_DEFAULT
        );
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(channel);
        }

    }
}