package com.sems.mical.data

import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.util.Log

open class LocationUpdateIntentService : LocationListener {
    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {

    }

    override fun onProviderEnabled(p0: String?) {
    }

    override fun onProviderDisabled(p0: String?) {
    }

    override fun onLocationChanged(p0: Location?) {
        if(p0 != null){

            val result = p0
        }else{
            Log.e("AAAA","No locationResult")
        }    }



    protected val TAG = LocationUpdateIntentService::class.java.simpleName


}