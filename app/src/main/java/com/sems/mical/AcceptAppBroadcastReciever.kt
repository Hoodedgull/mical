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
import com.sems.mical.data.entities.Fence
import java.time.LocalDateTime


class AcceptAppBroadcastReciever : BroadcastReceiver() {

    override fun onReceive(p0: Context?, p1: Intent?) {
        val action = p1?.getStringExtra("action")!!
        val latitude = p1?.getDoubleExtra("latitude", -1.0)
        val longitude = p1?.getDoubleExtra("longitude", -1.0)
        val radius = p1?.getDoubleExtra("radius", -1.0)
        val id = p1?.getIntExtra("id", -1)

        val string = p0?.getString(R.string.accept_button_in_the_notification_text)
        if (action == string) {

            handleRequest(p0, "", latitude, longitude, radius, true)
            Toast.makeText(
                p0?.applicationContext,
                "Mic access has been allowed in this location",
                Toast.LENGTH_SHORT
            ).show()

            val reminder = Reminder(getUniqueId().toString(), LatLng(latitude,longitude),200.0,"TEST",true)
            (p0?.applicationContext as MicalApp).getRepository().add(reminder,null, null);


            Log.e("BBBB", "ACCEPTED APPPPPPPPPP")
        } else if (action ==  p0?.getString(R.string.notok_button_in_the_notification_text)) {
            handleRequest(p0, "", latitude, longitude, radius, false)
            Toast.makeText(
                p0?.applicationContext,
                "Mic access has been revoked in this location",
                Toast.LENGTH_SHORT
            ).show()

            Log.e("BBBB", "DECLINED!!! APPPPPPPPPP")
        }



        with(NotificationManagerCompat.from(p0!!)) {
            // notificationId is a unique int for each notification that you must define
            cancel(id)
        }

        val apps = AppDatabase.getInstance(p0!!)!!.fenceDao().getId();
        Log.e("HELP", apps.size.toString())
    }

    private fun handleRequest(p0: Context?, id: String, lat: Double, long: Double, radius: Double, permission: Boolean){
        Log.e("BBBB", "I AM DB")
        var dbInstance = AppDatabase.getInstance(p0!!)!!.fenceDao()
        if (dbInstance.getFenceById(id).isEmpty() ){
            dbInstance.insertId(Fence(id, lat, long, radius, permission, LocalDateTime.now().toString()))
        }
    }
}

