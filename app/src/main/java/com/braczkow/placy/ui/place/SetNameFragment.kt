package com.braczkow.placy.ui.place


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs

import com.braczkow.placy.R
import com.braczkow.placy.base.App
import com.braczkow.placy.feature.place.PlaceApi
import com.braczkow.placy.feature.util.DispatchersFactory
import com.braczkow.placy.feature.util.DoOnStop
import com.google.android.gms.maps.model.LatLng
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import kotlinx.android.synthetic.main.fragment_set_name.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class SetNameView(private val rootView: View) {
    fun saveClicks(clicks: () -> Unit) {
        rootView.place_name_save_btn.setOnClickListener { clicks() }
    }

    fun getName(): String = rootView.place_name_edit.text.toString()
}

interface SetNameNavigation {
    sealed class Event {
        object Finish: Event()
    }

    fun navigate(event: Event)
}

class SetNamePresenter @Inject constructor(
    private val placeApi: PlaceApi,
    private val df: DispatchersFactory,
    private val lifecycle: Lifecycle,
    private val view: SetNameView,
    private val navigation: SetNameNavigation,
    private val latLng: LatLng
) {

    private val uiScope = CoroutineScope(df.main())

    init {

        DoOnStop(lifecycle) {
            uiScope.coroutineContext.cancelChildren()
        }


        view.saveClicks {
            if (view.getName().isEmpty()) {
                Timber.d("Empty name")
                return@saveClicks
            }

            uiScope.launch {
                val result = placeApi.createPlace(PlaceApi.PlaceData(view.getName(), latLng))
                Timber.d("Result is $result")

                navigation.navigate(SetNameNavigation.Event.Finish)
            }
        }
    }

}

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
            .plus(DaggerModule(lifecycle, SetNameView(view), args.latLng, createNavigation()))
            .build()
            .inject(this)


        return view
    }

    private fun createNavigation(): SetNameNavigation = object : SetNameNavigation {
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
