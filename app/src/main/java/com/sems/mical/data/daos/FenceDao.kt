package com.sems.mical.data.daos

import androidx.room.*
import com.sems.mical.data.entities.Fence

@Dao
interface FenceDao {

    @Query("SELECT * FROM Fence")
    fun getId() : List<Fence>

    @Insert
    fun insertId(app:Fence)

    @Update
    fun updateId(app:Fence)

    @Delete
    fun deleteId(app:Fence)

    @Query("SELECT * FROM Fence WHERE id == :id")
    fun getFenceById(id: String): List<Fence>
}