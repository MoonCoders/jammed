package com.github.mooncoders.jammed

import android.app.Application
import com.github.mooncoders.jammed.sdk.JammedSdk

class App : Application() {

    companion object {
        lateinit var sdk: JammedSdk
    }

    override fun onCreate() {
        super.onCreate()

        sdk = JammedSdk(this, "https://tbd")
    }
}