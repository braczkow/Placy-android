package com.braczkow.placy.base

import android.app.Application

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent
            .builder()
            .appModule(AppModule(applicationContext))
            .build()
    }

    companion object {
        private lateinit var appComponent : AppComponent
        fun dagger() = appComponent
    }
}
