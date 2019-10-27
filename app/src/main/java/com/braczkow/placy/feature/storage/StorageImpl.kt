package com.braczkow.placy.feature.storage

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import javax.inject.Inject

class StorageImpl @Inject constructor(
    private val context: Context
) : Storage {
    private val gson = Gson()

    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
    }

    override fun <T> load(type: Class<T>): T? {
        if (prefs.contains(type.simpleName)) {
            return gson.fromJson(prefs.getString(type.simpleName, ""), type)
        } else {
            return null
        }
    }

    override fun <T : Any> save(item: T) {
        prefs
            .edit()
            .putString(item::class.java.simpleName, gson.toJson(item))
            .apply()
    }
}