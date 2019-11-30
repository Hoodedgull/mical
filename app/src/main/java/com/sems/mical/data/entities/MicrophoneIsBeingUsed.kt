package com.sems.mical.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
@Entity
class MicrophoneIsBeingUsed (
        var fenceName: String = ""){
        var count: Int = 0
        @PrimaryKey(autoGenerate = true) var id : Int = 0
}
