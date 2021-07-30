package com.staytuned.demo

import android.app.Application
import com.staytuned.sdk.Staytuned
import com.staytuned.sdk.features.STContents
import com.staytuned.sdk.models.options.STOptions

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        // Init Staytuned SDK
        Staytuned.init(
            this,
            BuildConfig.appId,
            BuildConfig.appToken,
            STOptions()
        )

        //Set content caching configuration
        STContents.getInstance()
            ?.configuration
            ?.contentCachingOptions
            ?.apply {
                enabledContentCaching = true
                maxCachedContent = 5
            }
    }
}
