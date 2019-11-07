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





class AcceptAppBroadcastReciever : BroadcastReceiver() {
    private var count = 38;
    override fun onReceive(p0: Context?, p1: Intent?) {

        val toast = Toast.makeText(
            p0?.applicationContext,
            "This is a message displayed in a Toast",
            Toast.LENGTH_SHORT
        )

        toast.show()

        Log.e("BBBB", "ACCEPTED APPPPPPPPPP")


    }


}