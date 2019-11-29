package com.sems.mical.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
//import com.sems.mical.data.daos.AppDao
import com.sems.mical.data.daos.FenceDao
import com.sems.mical.data.daos.GeoFenceDao
import com.sems.mical.data.daos.MicUsedDao
import com.sems.mical.data.entities.Fence
import com.sems.mical.data.entities.GeoFence
import com.sems.mical.data.entities.MicrophoneIsBeingUsed

@Database(entities = [Fence::class, MicrophoneIsBeingUsed::class, GeoFence::class ],exportSchema = false,version = 6)
abstract class AppDatabase : RoomDatabase() {
    companion object {
    val DB_NAME = "App_Db"

    var instance :AppDatabase?= null

       fun getInstance(ctx: Context ) : AppDatabase? {
           if(instance == null){
               instance = Room.databaseBuilder(ctx.applicationContext, AppDatabase::class.java,DB_NAME )
                   .fallbackToDestructiveMigration()
                   .allowMainThreadQueries()
                   .build()
           }
           return instance;
        }
    }

    //abstract fun appDao() : AppDao ;
    abstract fun micUsedDao() : MicUsedDao ;
    abstract fun fenceDao(): FenceDao
    abstract fun geoFenceDao(): GeoFenceDao
}

