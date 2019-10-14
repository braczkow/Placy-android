package com.braczkow.placy.base

import android.app.Application
import com.braczkow.placy.BuildConfig
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

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
