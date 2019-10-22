package com.braczkow.placy.ui.place.set_name

import android.view.View

class SetNameView(private val rootView: View) {
    fun saveClicks(clicks: () -> Unit) {
        rootView.place_name_save_btn.setOnClickListener { clicks() }
    }

    fun getName(): String = rootView.place_name_edit.text.toString()
}