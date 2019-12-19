package com.sems.mical

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.JobIntentService
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import com.sems.mical.data.AppDatabase
import com.sems.mical.data.entities.GeoFence

class GeofenceTransitionsJobIntentService : JobIntentService() {

    override fun onHandleWork(intent: Intent) {
        // 1
        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        // 2
        if (geofencingEvent.hasError()) {
            val errorMessage = GeofenceErrorMessages.getErrorString(this,
                geofencingEvent.errorCode)
            Log.e(LOG_TAG, errorMessage)
            return
        }
        // 3
        handleEvent(geofencingEvent)
    }

    private fun handleEvent(event: GeofencingEvent) {
        // 1
        if (event.geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            // 2
            val reminder = getFirstReminder(event.triggeringGeofences)
            val message = "ENTER:" + reminder?.message
            val latLng = reminder?.latLng
            if (message != null && latLng != null) {
                // 3
                sendNotification(this, message, latLng)
                newFence(reminder)
            }
        } else if(event.geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT){
            val reminder = getFirstReminder(event.triggeringGeofences)
            val message = "Exit:" + reminder?.message
            val latLng = reminder?.latLng
            if (message != null && latLng != null) {
                // 3
                sendNotification(this, message, latLng)
                removeFence(reminder)
            }
        }else {
            val reminder = getFirstReminder(event.triggeringGeofences)
            val message =  "DWELLL" + reminder?.message
            val latLng = reminder?.latLng
            if (message != null && latLng != null) {
                // 3
                sendNotification(this, message, latLng)
            }
        }
    }

    private fun removeFence(reminder: Reminder) {
        AppDatabase.getInstance(this)!!.geoFenceDao().deleteOnExit(reminder.id)
    }

    private fun newFence(reminder: Reminder) {
        AppDatabase.getInstance(this)!!.geoFenceDao()
            .insertFence(GeoFence(reminder.id, reminder.title, reminder.allowed))
    }

    private fun getFirstReminder(triggeringGeofences: List<Geofence>): Reminder? {
        val firstGeofence = triggeringGeofences[0]
        return (application as MicalApp).getRepository().get(firstGeofence.requestId)
    }

    companion object {
        private const val LOG_TAG = "GeoTrIntentService"

        private const val JOB_ID = 573

        fun enqueueWork(context: Context, intent: Intent) {
            enqueueWork(
                context,
                GeofenceTransitionsJobIntentService::class.java, JOB_ID,
                intent)
        }
    }


}