package com.braczkow.placy.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.braczkow.placy.R
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
