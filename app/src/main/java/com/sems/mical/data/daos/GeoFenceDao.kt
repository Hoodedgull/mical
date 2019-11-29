package com.sems.mical.data.daos

import androidx.room.*
import com.sems.mical.data.entities.Fence
import com.sems.mical.data.entities.GeoFence

@Dao
interface GeoFenceDao {

    @Query("SELECT * FROM GeoFence")
    fun getAll() : List<GeoFence>

    @Insert
    fun insertFence(geoFence: GeoFence)

    @Update
    fun updateId(geoFence:GeoFence)

    @Delete
    fun deleteId(geoFence:GeoFence)

    @Query("DELETE FROM GeoFence WHERE id == :id")
    fun deleteOnExit(id: String)

    @Query("SELECT * FROM GeoFence WHERE id == :id")
    fun getFenceById(id: String): List<GeoFence>

    @Query("SELECT * FROM GeoFence WHERE title == :title")
    fun getFenceByTitle(title: String): List<GeoFence>
}