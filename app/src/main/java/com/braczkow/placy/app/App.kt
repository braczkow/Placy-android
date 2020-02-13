package com.braczkow.placy.app

import android.app.Application
import com.braczkow.placy.BuildConfig
import com.braczkow.platform.di.DaggerPlatformComponent
import com.braczkow.platform.di.PlatformModule
import com.braczkow.placy.feature.place.GeofencePlaceApi
import com.braczkow.platform.network.api.BluetoothApi
import timber.log.Timber
import javax.inject.Inject

class App : Application() {

    @Inject
    lateinit var geofencePlaceApi: GeofencePlaceApi

    @Inject
    lateinit var bluetoothApi: BluetoothApi

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        val locationComponent = DaggerPlatformComponent
            .builder()
            .plus(PlatformModule(applicationContext))
            .build()

        appComponent = DaggerAppComponent
            .builder()
            .appModule(AppModule(applicationContext))
            .plus(locationComponent)
            .build()

        appComponent.inject(this)

        bluetoothApi.start()
    }

    companion object {
        private lateinit var appComponent : AppComponent
        fun dagger() = appComponent
    }
}
