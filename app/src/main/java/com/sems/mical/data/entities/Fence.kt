package com.sems.mical.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Clock
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@Entity
class Fence(
    @PrimaryKey(autoGenerate = false)
    var id: String = "",
    var lat: Double = 0.0,
    var long: Double = 0.0,
    var radius: Double = 0.0,
    var isAcceptedByUser: Boolean = false,
    var timeUserGaveFeedback: String = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME) )