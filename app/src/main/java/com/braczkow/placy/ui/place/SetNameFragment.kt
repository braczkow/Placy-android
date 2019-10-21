package com.braczkow.placy.ui.place


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.navArgs

import com.braczkow.placy.R
import timber.log.Timber

class SetNameFragment : Fragment() {

    val args: SetNameFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_set_name, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Timber.d("args.latLng: ${args.latLng}")

        val backCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Timber.d("Handling back button")
            }
        }

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(backCallback)
    }
}
