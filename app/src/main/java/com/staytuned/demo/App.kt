package com.staytuned.demo

import android.app.Application
import com.staytuned.sdk.Staytuned
import com.staytuned.sdk.models.options.STOptions

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        Staytuned.init(applicationContext, "de03f2b8-ef81-428e-a20c-fb5becf32cad", "dda710aa.74ff4f0a-cf6b-4db1-9345-e88862ca7ce9", STOptions().apply {
            contentCachingOptions.enabledContentCaching = true
            contentCachingOptions.maxCachedContent = 5
        })

    }
}