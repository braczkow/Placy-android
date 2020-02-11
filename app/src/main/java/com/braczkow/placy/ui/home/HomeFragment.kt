package com.braczkow.placy.ui.home


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import com.braczkow.placy.R
import com.braczkow.placy.app.App
import com.braczkow.placy.app.ViewModelFactory
import com.braczkow.placy.feature.place.PlaceListApi
import com.braczkow.placy.platform.network.api.BluetoothApi
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


class HomeFragment : Fragment() {

    class ViewModel @Inject constructor(
        getPlaceListApi: PlaceListApi
    ): androidx.lifecycle.ViewModel() {

        private val placeList = MutableLiveData<List<PlaceListApi.PlaceListEntry>>()
        fun getPlaceList(): LiveData<List<PlaceListApi.PlaceListEntry>> = placeList


        init {

        }

        override fun onCleared() {
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var bluetoothApi: BluetoothApi

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        App
            .dagger()
            .inject(this)

        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val vm = ViewModelProviders.of(this, viewModelFactory).get(HomeFragment.ViewModel::class.java)

        vm.getPlaceList().observe(viewLifecycleOwner, Observer<List<PlaceListApi.PlaceListEntry>> {
            Timber.d("vm.getPlaceList observer in Fragment, size: ${it.size}")
        })


        view.home_create_place_btn.setOnClickListener {
            findNavController()
                .navigate(R.id.action_homeFragment_to_createPlaceFragment)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            bluetoothApi
                .networkState()
                .collect {
                    view.home_bluetooth_debug.setText("Bluetooth is: $it")
                }
        }

        return view
    }
}
