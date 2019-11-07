package com.sems.mical.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
@Entity
class MicrophoneIsBeingUsed (
        var appFullName: String = "",
        var timeUserGaveFeedback: String = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME) ){

        @PrimaryKey(autoGenerate = true) var id : Int = 0
}
