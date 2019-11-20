package com.sems.mical

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.sems.mical.data.AppDatabase
import com.sems.mical.data.entities.App
import com.sems.mical.data.entities.MicrophoneIsBeingUsed
import sensorapi.micapi.MicUsedImpl
import java.time.LocalDateTime
import java.util.*


class MicMonitoringService() : Service() {
    override fun onBind(p0: Intent?): IBinder? {
        return null;
    }


    companion object {
        val context = this
    }

    var notifID = 1234

    var timer:Timer? = null
    var task: TimerTask? = null
    override fun onCreate() {
        super.onCreate();
        val delay:Long = 1000 // delay for 1 sec.
        val period = 1000L // repeat every sec.

        var foregroundbuilder = NotificationCompat.Builder(this, "hello")
            .setSmallIcon(R.drawable.ic_stat_onesignal_default)
            .setContentTitle("Safe")
            .setContentText("Your privacy is safe")
            .setPriority(NotificationCompat.PRIORITY_LOW)
        startForeground(8,foregroundbuilder.build())



        timer = Timer()

        task = object : TimerTask() {
            override fun run() {
            monitorMic();
            }
        }

        timer!!.scheduleAtFixedRate(task, delay, period)

    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel();
        task?.cancel();
    }

     fun monitorMic() {

         var micUsedImpl = MicUsedImpl()
         var response = micUsedImpl.isMicBeingUsed()

         if (response.result) {

             if (AppDatabase.getInstance(this)!!.appDao().getAppByName(response.appName).isNotEmpty())
                 return

             Log.e("Service ID", response.appName.hashCode().toString())
             Log.e("Service Name", response.appName)

                 val acceptIntent = Intent(this, AcceptAppBroadcastReciever::class.java).apply {
                     action = "com.sems.mical.micallow"
                     putExtra("id", response.appName.hashCode())
                     putExtra("action", "accept");
                     putExtra("appname", response.appName);

                 }


                 val acceptPendingIntent: PendingIntent =
                     PendingIntent.getBroadcast(this, 123, acceptIntent, PendingIntent.FLAG_UPDATE_CURRENT)

                 val declineIntent = Intent(this, AcceptAppBroadcastReciever::class.java).apply {
                     action = "com.sems.mical.micdeny"
                     putExtra("id", response.appName.hashCode())
                     putExtra("action", "decline")
                     putExtra("appname", response.appName)
                 }


                 val declinePendingIntent: PendingIntent =
                     PendingIntent.getBroadcast(this, 123, declineIntent, PendingIntent.FLAG_UPDATE_CURRENT)

                 var builder = NotificationCompat.Builder(this, "hello")
                     .setSmallIcon(R.drawable.ic_stat_onesignal_default)
                     .setContentTitle(response.appName)
                     .setContentText("Wants to use the mic")
                     .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                     .addAction(R.drawable.ic_stat_onesignal_default, "Accept", acceptPendingIntent)
                     .addAction(
                         R.drawable.ic_stat_onesignal_default,
                         "Decline",
                         declinePendingIntent
                     )
                     .setAutoCancel(true)





                 with(NotificationManagerCompat.from(this)) {
                     // notificationId is a unique int for each notification that you must define
                     notify(response.appName.hashCode(), builder.build())

                 }

                 AppDatabase.getInstance(this)!!.micUsedDao().insert(
                     MicrophoneIsBeingUsed(
                         response.appName,
                         LocalDateTime.now().toString()
                     )
                 )
             }

         }

}