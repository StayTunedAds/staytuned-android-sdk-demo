package com.staytuned.demo

import android.app.Application
import com.staytuned.sdk.Staytuned

class App: Application() {

    override fun onCreate() {
        super.onCreate()

        Staytuned.init(applicationContext, "38b18a01-bcfc-46cf-a0e2-121b2ed994c4", "14f26bdb.ece3fa09-b674-428a-8f92-83a15bb5efd7")
        // Staytuned.init(applicationContext, "de03f2b8-ef81-428e-a20c-fb5becf32cad", "0d782c95.f09af78a-547d-493f-b017-f689a2e52f74")

    }
}