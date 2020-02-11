package com.braczkow.placy.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Provider

class ViewModelFactory @Inject
constructor(private val creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val creator = creators[modelClass]
        if (creator != null) {
            Timber.d("Creator not null, get ViewModel")
            return creator.get() as T
        }

        throw RuntimeException("ViewModel creator not registered")
    }
}
