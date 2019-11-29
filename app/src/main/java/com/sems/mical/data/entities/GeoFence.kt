package com.sems.mical.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Clock
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@Entity
class GeoFence(
    @PrimaryKey(autoGenerate = false)
    var id: String = "",
    var title: String = "")