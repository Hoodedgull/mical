package com.sems.mical

import android.annotation.SuppressLint
import android.app.IntentService
import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Criteria
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.IBinder
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.sems.mical.data.AppDatabase
import com.sems.mical.data.entities.App
import com.sems.mical.data.entities.MicrophoneIsBeingUsed
import sensorapi.micapi.MicUsedImpl
import java.time.LocalDateTime
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sems.mical.data.LocationUpdateIntentService
import sensorapi.micapi.PermissionListing
import java.util.*


class MicMonitoringService() : Service() {
    override fun onBind(p0: Intent?): IBinder? {
        return null;
    }




    var notifCount = 1;

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

    fun getLocation(): Location?{

        var lo: Location? = null
        val locationManager = getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager;

        val criteria = Criteria()

        criteria.accuracy = Criteria.ACCURACY_FINE
        criteria.isAltitudeRequired = false
        criteria.isBearingRequired = false
        criteria.isSpeedRequired = false

        try{
            val provider = locationManager.getBestProvider(criteria,true)


            if (Looper.myLooper() == null)
            {
                Looper.prepare();
            }

            if(provider != null){

                locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER,
                    LocationUpdateIntentService(),null)
                lo= locationManager.getLastKnownLocation(provider)
            }else{
                lo = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)


            }
        }catch(Ex:SecurityException){

        }

        return lo
    }

    var cnt = 1
     fun monitorMic() {

        var micUsedImpl = MicUsedImpl()
        var response = micUsedImpl.isMicBeingUsed()
        if (response.result||true) {

            var permissionName = PermissionListing()
            val appName = permissionName.getPermissionApp(this)

            Log.e("AAAA", "Result!")
            var locationUser = getLocation()!!


            // location is in a bad place
            if(true){
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                val uri = Uri.fromParts("package", packageName, null)
                intent.setData(uri)
                startActivity(intent)
            }


            val acceptIntent = Intent(this, AcceptAppBroadcastReciever::class.java).apply {
                action = "com.sems.mical.micallow"
                putExtra(Notification.EXTRA_NOTIFICATION_ID, notifCount++)
                putExtra("action", "accept");
                putExtra("appname", response.appName);
                putExtra("latitude",locationUser.latitude);
                putExtra("longitude",locationUser.longitude);

                 val acceptIntent = Intent(this, AcceptAppBroadcastReciever::class.java).apply {
                     action = "com.sems.mical.micallow"
                     putExtra("id", response.appName.hashCode())
                     putExtra("action", "accept");
                     putExtra("appname", response.appName);

                 }


            val acceptPendingIntent: PendingIntent =
                PendingIntent.getBroadcast(this, notifCount, acceptIntent, 0)

            val declineIntent = Intent(this, AcceptAppBroadcastReciever::class.java).apply {
                action = "com.sems.mical.micallow"
                putExtra(Notification.EXTRA_NOTIFICATION_ID, notifCount)
                putExtra("action", "decline")
                putExtra("appname", response.appName)
                putExtra("latitude",locationUser.latitude);
                putExtra("longitude",locationUser.longitude);
            }


                 val declinePendingIntent: PendingIntent =
                     PendingIntent.getBroadcast(this, 123, declineIntent, PendingIntent.FLAG_UPDATE_CURRENT)

            val declinePendingIntent: PendingIntent =
                PendingIntent.getBroadcast(this, notifCount, declineIntent, 0)



            var builder = NotificationCompat.Builder(this, "hello")
                .setSmallIcon(R.drawable.ic_stat_onesignal_default)
                .setContentTitle(appName)
                .setContentText("Wants to use the mic")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .addAction(R.drawable.ic_stat_onesignal_default, "Accept",acceptPendingIntent)
                .addAction(R.drawable.ic_stat_onesignal_default, "Decline", declinePendingIntent)
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

            with(NotificationManagerCompat.from(this)) {
                // notificationId is a unique int for each notification that you must define
                notify(notifCount, builder.build())
            }

            AppDatabase.getInstance(this)!!.micUsedDao().insert(MicrophoneIsBeingUsed(appName,LocalDateTime.now().toString()))
        }

    }
}