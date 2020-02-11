package com.braczkow.placy.feature.db

import androidx.room.Entity
import androidx.room.PrimaryKey

//TODO: enumize?
val EVENT_TYPE_ENTER = 0
val EVENT_TYPE_EXIT = 1

@Entity
data class PlaceEvent(
    @PrimaryKey(autoGenerate = true) val eventId: Long,
    val placeId: String,
    val eventType: Int,
    val ts: Long
)