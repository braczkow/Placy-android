package com.braczkow.placy.ui


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.braczkow.placy.R
import com.braczkow.placy.feature.LocationApi
import com.braczkow.placy.feature.di.SomeApi
import com.google.android.gms.maps.SupportMapFragment
import kotlinx.android.synthetic.main.fragment_home.view.*
import javax.inject.Inject


class HomeFragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        view.home_create_place_btn.setOnClickListener {
            findNavController()
                .navigate(R.id.action_homeFragment_to_createPlaceFragment)
        }

        return view
    }




}
