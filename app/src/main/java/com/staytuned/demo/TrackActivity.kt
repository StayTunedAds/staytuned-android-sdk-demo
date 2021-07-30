package com.staytuned.demo

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.staytuned.sdk.features.STContents
import com.staytuned.sdk.http.STHttpCallback
import com.staytuned.sdk.models.STContent
import com.staytuned.sdk.ui.STTrackDetailFragment
import kotlinx.android.synthetic.main.activity_track.*

class TrackActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_track)

        val contentKey: String? = intent.getStringExtra(EXTRA_CONTENT_KEY)
        contentKey ?: run {
            Log.e(LOG_TAG, "Cannot fetch data with null contentKey")
            finish()
            return
        }

        val trackKey: String? = intent.getStringExtra(EXTRA_TRACK_KEY)
        trackKey ?: run {
            Log.e(LOG_TAG, "Cannot fetch data with null trackKey")
            finish()
            return
        }

        showLoading()

        fetchData(trackKey, contentKey)
    }

    private fun fetchData(trackKey: String, contentKey: String) {
        STContents.getInstance()?.getContent(contentKey, object : STHttpCallback<STContent> {
            override fun onError(t: Throwable) {
                hideLoading()
                Log.e(LOG_TAG, "Error fetching content with content key $contentKey : ", t)
                finish()
            }

            override fun onSuccess(data: STContent) {
                hideLoading()
                data.elementList?.find { it.key == trackKey }?.let { stTrack ->
                    val fragmentManager: FragmentManager = supportFragmentManager
                    val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
                    val fragment = STTrackDetailFragment.withContentAndTrack(data, stTrack)
                    fragmentTransaction.add(
                        R.id.track_container,
                        fragment,
                        TAG_TRACK_FRAGMENT
                    )
                    fragmentTransaction.commit()
                } ?: kotlin.run {
                    Log.e(LOG_TAG, "No track with key $trackKey in content with content key $contentKey")
                    finish()
                }
            }
        })
    }

    private fun showLoading() {
        loader.visibility = View.VISIBLE
        track_wrapper.visibility = View.GONE
    }

    private fun hideLoading() {
        track_wrapper.visibility = View.VISIBLE
        loader.visibility = View.GONE
    }
}

private const val LOG_TAG = "TrackActivity"

private const val TAG_TRACK_FRAGMENT = "trackFragment"
