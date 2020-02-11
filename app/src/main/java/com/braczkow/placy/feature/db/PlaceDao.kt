package com.braczkow.placy.feature.db

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface PlaceDao {
    @Insert
    suspend fun addEvents(vararg events: PlaceEvent)
}