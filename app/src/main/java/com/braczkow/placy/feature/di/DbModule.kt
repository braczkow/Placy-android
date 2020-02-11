package com.braczkow.placy.feature.di

import android.content.Context
import androidx.room.Room
import com.braczkow.placy.app.AppScope
import com.braczkow.placy.feature.db.AppDatabase
import dagger.Module
import dagger.Provides

@Module
class DbModule {
    @Provides
    @AppScope
    fun appDb(context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java, "app.db"
        ).build()
    }

    @Provides
    fun placeDao(db: AppDatabase) = db.placeDao()
}