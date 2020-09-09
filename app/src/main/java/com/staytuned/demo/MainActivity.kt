package com.staytuned.demo

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.MemoryCategory
import com.google.gson.Gson
import com.staytuned.demo.viewmodels.MainViewModel
import com.staytuned.sdk.features.STAuth
import com.staytuned.sdk.features.STContents
import com.staytuned.sdk.http.STHttpCallback
import com.staytuned.sdk.models.STAuthResponse
import com.staytuned.sdk.models.STContent
import com.staytuned.sdk.models.STContentLight
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val vModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Glide.get(this).setMemoryCategory(MemoryCategory.LOW)

        vModel.getSections()

        /*
        STAuth.getInstance()?.refresh(object : STHttpCallback<STAuthResponse> {
            override fun onSuccess(data: STAuthResponse) {
                Toast.makeText(this@MainActivity, "Connected !", Toast.LENGTH_SHORT).show()
                println(Gson().toJson(data))
                vModel.getLists()
            }

            override fun onError(t: Throwable) {
                t.printStackTrace()
                Toast.makeText(this@MainActivity, "Failed to connect", Toast.LENGTH_SHORT).show()
            }
        }) */
 
        val navController = findNavController(R.id.fragNavHost)
        bottomNavView.setupWithNavController(navController)

        val appBarConfiguration = AppBarConfiguration(
            topLevelDestinationIds = setOf(
                R.id.homeFragment,
                R.id.contentListFragment,
                R.id.trackListFragment
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
    }
}
