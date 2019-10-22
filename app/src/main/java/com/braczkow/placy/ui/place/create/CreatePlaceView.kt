package com.braczkow.placy.ui.place.create

import android.view.View
import com.braczkow.placy.ui.util.visible
import kotlinx.android.synthetic.main.fragment_create_place.view.*

class CreatePlaceView(private val rootView: View) {
    fun showAddress(result: String) {
        rootView.map_address_text.setText(result)
    }

    fun showProceedButton() {
        rootView.map_proceed_frame.visible()
    }

    fun proceedBtnClicks(clicks: () -> Unit) {
        rootView.map_proceed_btn.setOnClickListener { clicks() }
    }

}