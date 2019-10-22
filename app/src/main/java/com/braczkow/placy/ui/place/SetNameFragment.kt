package com.braczkow.placy.ui.place


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.navArgs

import com.braczkow.placy.R
import com.braczkow.placy.base.App
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import timber.log.Timber
import javax.inject.Inject

class SetNameView(private val rootView: View)

class SetNamePresenter @Inject constructor(
    private val lifecycle: Lifecycle,
    private val view: SetNameView
) {

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
        val view: SetNameView
    ) {
        @Provides
        fun provideLifecycle() = lifecycle

        @Provides
        fun provideView() = view
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
            .plus(DaggerModule(lifecycle, SetNameView(view)))
            .build()
            .inject(this)


        return view
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
