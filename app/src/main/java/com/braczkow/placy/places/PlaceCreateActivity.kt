package com.braczkow.placy.places

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.braczkow.placy.R
import com.braczkow.placy.base.App
import com.braczkow.placy.places.di.SomeApi
import com.google.android.gms.maps.SupportMapFragment
import kotlinx.android.synthetic.main.activity_place_create.*
import javax.inject.Inject


class PlaceCreateActivity : AppCompatActivity() {

    @Inject
    lateinit var someApi: SomeApi

    val mapsApiKey = "AIzaSyCJdZNg8Aq0-i3UPcC2cw1lBCVto5tjJcg"

    val TAG = "PLC " + PlaceCreateActivity::class.java.simpleName

    private lateinit var mapFragment: SupportMapFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        App.dagger().placeComponentBuilder().build().inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_create)

        this.place_create_api_txt.setText(someApi.haveFun())

        mapFragment = supportFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync { map ->
            Log.d(TAG, "Got map!")
        }
    }


}
