package com.sems.mical.data.daos

import androidx.room.*
import com.sems.mical.data.entities.App

@Dao
interface AppDao {

    @Query("SELECT * FROM App")
    fun getApps() : List<App>

    @Insert
    fun insertApp(app:App)

    @Update
    fun updateApp(app:App)

    @Delete
    fun deleteApp(app:App)

    @Query("SELECT * FROM App WHERE displayName == :name")
    fun getAppByName(name: String): List<App>
}