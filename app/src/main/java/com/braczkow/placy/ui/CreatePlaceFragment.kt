package com.braczkow.placy.ui


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.braczkow.placy.R
import com.braczkow.placy.base.App
import com.braczkow.placy.feature.LocationApi
import com.braczkow.placy.feature.di.SomeApi
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.fragment_create_place.view.*
import javax.inject.Inject

@Subcomponent(modules = [PlaceCreateFragmentModule::class])
interface PlaceCreateFragmentDaggerComponent {
    @Subcomponent.Builder
    interface Builder {
        fun build(): PlaceCreateFragmentDaggerComponent
        fun plus(module: PlaceCreateFragmentModule): Builder
    }

    fun inject(placeCreateFgmt: CreatePlaceFragment)
}

class FgmtView
class Presenter(private val view: FgmtView)

@Module
class PlaceCreateFragmentModule(private val view: FgmtView) {
    @Provides
    fun providePresenter() = Presenter(view)
}


class CreatePlaceFragment : Fragment() {
    @Inject
    lateinit var someApi: SomeApi

    @Inject
    lateinit var locationApi: LocationApi

    @Inject
    lateinit var presenter: Presenter

    private lateinit var mapFragment: SupportMapFragment

    private val TAG = "PLC " + CreatePlaceFragment::class.java.simpleName

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_create_place, container, false)

        view.place_create_api_txt.setText(someApi.haveFun())

        mapFragment =  childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync { map ->
            Log.d(TAG, "Got map!")

            map.setOnMapLongClickListener {latLng ->
                Log.d(TAG, "Long click on: ${latLng}")

                moveMarkerTo(latLng)
            }

            markerMovementQueue
                .subscribe { mmr ->
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(mmr.latLng, mmr.zoom))
                }
        }



        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        App
            .dagger()
            .placeComponentBuilder()
            .plus(PlaceCreateFragmentModule(FgmtView()))
            .build()
            .inject(this)

        super.onCreate(savedInstanceState)
    }

    private fun moveMarkerTo(latLng: LatLng) {
        markerMovementQueue.onNext(
            MarkerMoveRequest(
                latLng,
                DEFAULT_ZOOM
            )
        )
    }

    private val markerMovementQueue = BehaviorSubject.create<MarkerMoveRequest>()

    private data class MarkerMoveRequest(val latLng: LatLng, val zoom : Float)

    companion object {
        private const val DEFAULT_ZOOM = 15.0f
    }
}
