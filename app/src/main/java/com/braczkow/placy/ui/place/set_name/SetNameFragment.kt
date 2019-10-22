package com.braczkow.placy.ui.place.set_name


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs

import com.braczkow.placy.R
import com.braczkow.placy.base.App
import com.braczkow.placy.ui.place.SetNameFragmentArgs
import com.google.android.gms.maps.model.LatLng
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Inject

class SetNameFragment : Fragment() {

    @Subcomponent(modules = [DaggerModule::class])
    interface DaggerComponent {
        @Subcomponent.Builder
        interface Builder {
            fun plus(module: DaggerModule): Builder
            fun build(): DaggerComponent
        }

        fun inject(fgmt: SetNameFragment)
    }

    @Module
    class DaggerModule(
        val lifecycle: Lifecycle,
        val view: SetNameView,
        val latLng: LatLng,
        val navigation: SetNameNavigation
    ) {
        @Provides fun provideLifecycle() = lifecycle

        @Provides fun provideView() = view

        @Provides fun provideLatLng() = latLng

        @Provides fun navigation() = navigation
    }


    @Inject
    lateinit var presenter: SetNamePresenter

    val args: SetNameFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_set_name, container, false)
        App
            .dagger()
            .setNameBuilder()
            .plus(
                DaggerModule(
                    lifecycle,
                    SetNameView(view),
                    args.latLng,
                    createNavigation()
                )
            )
            .build()
            .inject(this)


        return view
    }

    private fun createNavigation(): SetNameNavigation = object :
        SetNameNavigation {
        override fun navigate(event: SetNameNavigation.Event) {
            when (event) {
                is SetNameNavigation.Event.Finish -> {

                    findNavController()
                        .navigate(R.id.finish_place_flow)
                }
            }
        }
    }
}
