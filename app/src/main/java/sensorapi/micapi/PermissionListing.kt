package sensorapi.micapi

import android.content.pm.PackageManager
import android.content.pm.PackageInfo
import android.R.attr.name
import android.content.Context
import android.content.pm.ApplicationInfo
import android.util.Log


class PermissionListing {
    fun getPermissionApp(con: Context) {
        val pm = con.getPackageManager()
        val packages = pm.getInstalledApplications(PackageManager.GET_META_DATA)

        for (applicationInfo in packages) {
            Log.d(
                "test",
                "App: " + applicationInfo.name + " Package: " + applicationInfo.packageName
            )

            try {
                val packageInfo =
                    pm.getPackageInfo(applicationInfo.packageName, PackageManager.GET_PERMISSIONS)

                //Get Permissions
                val requestedPermissions = packageInfo.requestedPermissions

                if (requestedPermissions != null) {
                    for (i in requestedPermissions!!.indices) {
                        Log.d("test", requestedPermissions!![i])
                    }
                }
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }

        }
    }
}