package com.sems.mical

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
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
import com.sems.mical.data.entities.MicrophoneIsBeingUsed
import sensorapi.micapi.MicUsedImpl
import java.time.LocalDateTime
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.model.LatLng
import com.sems.mical.data.LocationUpdateIntentService
import sensorapi.micapi.PermissionListing
import java.util.*


class MicMonitoringService() : Service() {
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    companion object currentGeoFence{
        var fenceID = ""
    }




    var notifId = 10101;

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

        var bestLocation: Location? = null
        val locationManager = getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager;


        try{

            if (Looper.myLooper() == null)
            {
                Looper.prepare();
            }


                locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER,
                    LocationUpdateIntentService(),null)
                bestLocation= locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)


                locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, LocationUpdateIntentService(), null)
                val newLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

            // if more recent = more better
                if(newLocation.time > bestLocation.time){
                    bestLocation = newLocation
                }



        }catch(Ex:SecurityException){

        }

        return bestLocation
    }

     fun monitorMic() {

        var micUsedImpl = MicUsedImpl()
        var response = micUsedImpl.isMicBeingUsed()
         Log.e("fenceID",fenceID)
        if (response.result) {

            var permissionName = PermissionListing()
            val appName = permissionName.getPermissionApp(this)
            Log.e("fenceID",fenceID)
                if (AppDatabase.getInstance(this)!!.fenceDao().getFenceById(response.appName).isNotEmpty())
                    return

                var locationUser = getLocation()

                // location is in a bad place
                if (true) {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.setData(uri)
                    //startActivity(intent)
                }

                val lat =  locationUser?.latitude
            val long = locationUser?.longitude
            val latLong = lat?.let { long?.let { it1 -> LatLng(it, it1) } }
                val acceptIntent = Intent(this, AddGeofenceActivity::class.java).apply {
                    action = "com.sems.mical.micallow"
                    putExtra("id", notifId)
                    putExtra("action", applicationContext.getString(R.string.accept_button_in_the_notification_text));
                    putExtra("appname", response.appName);
                    putExtra("latitude",lat);
                    putExtra("longitude", long);
                    putExtra("EXTRA_LAT_LNG", latLong);
                }


                val acceptPendingIntent: PendingIntent =
                    PendingIntent.getActivity(this, 123, acceptIntent, PendingIntent.FLAG_CANCEL_CURRENT)

                val declineIntent = Intent(this, AddGeofenceActivity::class.java).apply {
                    action = "com.sems.mical.micallow"
                    putExtra("id", notifId)
                    putExtra("action", applicationContext.getString(R.string.notok_button_in_the_notification_text))
                    putExtra("appname", response.appName)
                    putExtra("latitude", locationUser?.latitude);
                    putExtra("longitude", locationUser?.longitude);
                }

                val declinePendingIntent: PendingIntent =
                    PendingIntent.getActivity(this, 124, declineIntent,  PendingIntent.FLAG_CANCEL_CURRENT)


                var builder = NotificationCompat.Builder(this, "hello")
                    .setSmallIcon(R.drawable.ic_stat_onesignal_default)
                    .setContentTitle(appName)
                    .setContentText("Wants to use the mic")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .addAction(R.drawable.ic_stat_onesignal_default, applicationContext.getString(R.string.accept_button_in_the_notification_text), acceptPendingIntent)
                    .addAction(
                        R.drawable.ic_stat_onesignal_default,
                        applicationContext.getString(R.string.notok_button_in_the_notification_text),
                        declinePendingIntent
                    )
                //.setAutoCancel(true)


                with(NotificationManagerCompat.from(this)) {
                    // notificationId is a unique int for each notification that you must define
                    notify(notifId, builder.build())
                }

                AppDatabase.getInstance(this)!!.micUsedDao()
                    .insert(MicrophoneIsBeingUsed("", LocalDateTime.now().toString()))
            }


    }
}