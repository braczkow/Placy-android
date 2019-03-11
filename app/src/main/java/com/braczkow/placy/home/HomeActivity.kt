package com.braczkow.placy.home

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.braczkow.placy.R
import com.braczkow.placy.places.PlaceCreateActivity
import kotlinx.android.synthetic.main.activity_home.*



class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        home_create_place_btn.setOnClickListener {
            startActivity(Intent(this, PlaceCreateActivity::class.java))
        }
    }
}
