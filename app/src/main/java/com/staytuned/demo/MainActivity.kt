package com.staytuned.demo

import android.content.Context
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
import com.staytuned.sdk.http.STHttpCallback
import com.staytuned.sdk.models.STAuthResponse
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.reflect.Method

class MainActivity : AppCompatActivity() {
    val vModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Glide.get(this).setMemoryCategory(MemoryCategory.LOW)

        vModel.getSections()
        showDebugDBAddressLogToast(this)

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
        })

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

    private fun showDebugDBAddressLogToast(context: Context?) {
        if (BuildConfig.DEBUG) {
            try {
                val debugDB = Class.forName("com.amitshekhar.DebugDB")
                val getAddressLog: Method = debugDB.getMethod("getAddressLog")
                val value: Any = getAddressLog.invoke(null)
                Toast.makeText(
                    context,
                    value as String,
                    Toast.LENGTH_LONG
                ).show()
            } catch (ignore: Exception) {
            }
        }
    }
}
