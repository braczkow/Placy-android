package com.braczkow.platform.location.internal

import android.content.Context
import javax.inject.Inject

interface SomeUtils

class SomeUtilsImpl @Inject constructor(
    private val context: Context
) : SomeUtils