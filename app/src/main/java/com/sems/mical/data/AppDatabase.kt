package com.sems.mical.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sems.mical.data.daos.AppDao
import com.sems.mical.data.entities.App

@Database(entities = [App::class],exportSchema = false,version = 1)
abstract class AppDatabase : RoomDatabase() {
    companion object {
    val DB_NAME = "App_Db"

    var instance :AppDatabase?= null

       fun getInstance(ctx: Context ) : AppDatabase? {
           if(instance == null){
               instance = Room.databaseBuilder(ctx.applicationContext, AppDatabase::class.java,DB_NAME )
                   .fallbackToDestructiveMigration()
                   .build()
           }
           return instance;
        }
    }

    abstract fun appDao() : AppDao ;
}

