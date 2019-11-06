package com.sems.mical

import android.app.IntentService
import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import sensorapi.micapi.MicUsedImpl

class MicMonitoringService() : IntentService("Bob") {
    override fun onHandleIntent(p0: Intent?) {
        Log.e("AAAA", "Got into handleIntent")

        var micUsedImpl = MicUsedImpl()
        var response = micUsedImpl.isMicBeingUsed()
        if (response.result) {



            val acceptIntent = Intent(this, AcceptAppBroadcastReciever::class.java).apply {
                action = "what is an action"
                putExtra(Notification.EXTRA_NOTIFICATION_ID, 0)
            }


            val acceptPendingIntent: PendingIntent =
                PendingIntent.getBroadcast(this, 0, acceptIntent, 0)


            var builder = NotificationCompat.Builder(this, "hello")
                .setSmallIcon(R.drawable.ic_stat_onesignal_default)
                .setContentTitle(response.appName)
                .setContentText("Wants to use the mic")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .addAction(R.drawable.ic_stat_onesignal_default,"actiontitle", acceptPendingIntent);

            with(NotificationManagerCompat.from(this)) {
                // notificationId is a unique int for each notification that you must define
                notify(1234, builder.build())
            }
        } else {
            var builder = NotificationCompat.Builder(this, "hello")
                .setSmallIcon(R.drawable.ic_stat_onesignal_default)
                .setContentTitle("Safe")
                .setContentText("Your privacy is safe")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            with(NotificationManagerCompat.from(this)) {
                // notificationId is a unique int for each notification that you must define
                notify(1234, builder.build())
            }
        }

    }
}