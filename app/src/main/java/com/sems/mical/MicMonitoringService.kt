package com.sems.mical

import android.app.IntentService
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.room.Room
import com.sems.mical.data.AppDatabase
import com.sems.mical.data.entities.App
import com.sems.mical.data.entities.MicrophoneIsBeingUsed
import sensorapi.micapi.MicUsedImpl
import java.time.LocalDateTime

class MicMonitoringService() : IntentService("Bob") {
    companion object {
        val context = this
    }

    var cnt = 1
    override fun onHandleIntent(p0: Intent?) {
        Log.e("AAAA", "Got into handleIntent")

        var micUsedImpl = MicUsedImpl()
        var response = micUsedImpl.isMicBeingUsed()
        if (response.result) {



            val acceptIntent = Intent(this, AcceptAppBroadcastReciever::class.java).apply {
                action = "com.sems.mical.micallow"
                putExtra(Notification.EXTRA_NOTIFICATION_ID, 123456789)
                putExtra("action", "accept");
                putExtra("appname", response.appName);
                sendBroadcast(this)
            } 



            val acceptPendingIntent: PendingIntent =
                PendingIntent.getBroadcast(this, 123, acceptIntent, 0)

            val declineIntent = Intent(this, AcceptAppBroadcastReciever::class.java).apply {
                action = "com.sems.mical.micallow"
                putExtra(Notification.EXTRA_NOTIFICATION_ID, 123456789)
                putExtra("action", "decline");
                putExtra("appname", response.appName);
                sendBroadcast(this)
            }



            val declinePendingIntent: PendingIntent =
                PendingIntent.getBroadcast(this, 123, declineIntent, 0)



            var builder = NotificationCompat.Builder(this, "hello")
                .setSmallIcon(R.drawable.ic_stat_onesignal_default)
                .setContentTitle(response.appName)
                .setContentText("Wants to use the mic")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .addAction(R.drawable.ic_stat_onesignal_default, "Accept",acceptPendingIntent)
                .addAction(R.drawable.ic_stat_onesignal_default, "Decline", declinePendingIntent);

            startForeground(123456789,builder.build())

            with(NotificationManagerCompat.from(this)) {
                // notificationId is a unique int for each notification that you must define
                notify(1234, builder.build()) 
            }

            AppDatabase.getInstance(this)!!.micUsedDao().insert(MicrophoneIsBeingUsed(response.appName,LocalDateTime.now().toString()))
        } else {
            var builder = NotificationCompat.Builder(this, "hello")
                .setSmallIcon(R.drawable.ic_stat_onesignal_default)
                .setContentTitle("Safe")
                .setContentText("Your privacy is safe")
                .setPriority(NotificationCompat.PRIORITY_LOW)


            startForeground(123456789,builder.build())

           }

    }
}