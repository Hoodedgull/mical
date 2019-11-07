package com.sems.mical

import android.app.IntentService
import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.sems.mical.data.AppDatabase
import com.sems.mical.data.entities.App
import sensorapi.micapi.MicUsedImpl

class MicMonitoringService() : IntentService("Bob") {
    var cnt = 1
    override fun onHandleIntent(p0: Intent?) {
        Log.e("AAAA", "Got into handleIntent")

        var micUsedImpl = MicUsedImpl()
        var response = micUsedImpl.isMicBeingUsed()
        if (response.result) {



            val acceptIntent = Intent(this, AcceptAppBroadcastReciever::class.java).apply {
                action = "what is an action"
                putExtra(Notification.EXTRA_NOTIFICATION_ID, 123456789)
            }


            val acceptPendingIntent: PendingIntent =
                PendingIntent.getBroadcast(this, 123, acceptIntent, 0)


            var builder = NotificationCompat.Builder(this, "hello")
                .setSmallIcon(R.drawable.ic_stat_onesignal_default)
                .setContentTitle(response.appName)
                .setContentText("Wants to use the mic")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .addAction(R.drawable.ic_stat_onesignal_default,"accept", acceptPendingIntent);

            startForeground(123456789,builder.build())

            with(NotificationManagerCompat.from(this)) {
                // notificationId is a unique int for each notification that you must define
                notify(1234, builder.build())
            }

            AppDatabase.getInstance(this)!!.appDao().insertApp(App("com.microsoft.skype"+cnt++,"SKYPE",false,"2019-11-10 14:12:00"))
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