package com.staytuned.demo

import android.app.Application
import android.content.Context
import com.staytuned.sdk.Staytuned
import com.staytuned.sdk.features.STContents
import com.staytuned.sdk.features.STPlayer
import com.staytuned.sdk.models.options.STOptions

class App : Application() {

    override fun onCreate() {
        super.onCreate() 
        Staytuned.init(applicationContext, BuildConfig.appId, BuildConfig.appToken, STOptions())
        STContents.getInstance()?.configuration?.contentCachingOptions?.apply {
            enabledContentCaching = true
            maxCachedContent = 5
        }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
    }
}