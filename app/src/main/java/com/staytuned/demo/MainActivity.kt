package com.staytuned.demo

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.gson.Gson
import com.staytuned.demo.viewmodels.MainViewModel
import com.staytuned.sdk.features.STAuth
import com.staytuned.sdk.http.STHttpCallback
import com.staytuned.sdk.models.STAuthResponse
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initNavigation()
        refreshData()

    }

    private fun initNavigation() {
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

    private fun refreshData() {
        mainViewModel.getSections()

        STAuth.getInstance()?.connectAnonymous(object : STHttpCallback<STAuthResponse> {
            override fun onSuccess(data: STAuthResponse) {
                Log.d(LOG_TAG, "Connection success ! Result data : ${Gson().toJson(data)} ")
                mainViewModel.getLists()
            }

            override fun onError(t: Throwable) {
                Log.e(LOG_TAG, "Error connecting : ", t)
                Toast.makeText(this@MainActivity, "Failed to connect", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

private const val LOG_TAG = "MainActivity"
