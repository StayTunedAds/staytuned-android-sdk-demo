package com.staytuned.demo

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.staytuned.sdk.features.STContents
import com.staytuned.sdk.http.STHttpCallback
import com.staytuned.sdk.models.STContent
import com.staytuned.sdk.models.STTrack
import com.staytuned.sdk.ui.STTrackDetailFragment
import kotlinx.android.synthetic.main.activity_track.*

class TrackActivity : AppCompatActivity() {

    private var content: STContent? = null
    private var track: STTrack? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_track)

        val contentKey: String? = intent.getStringExtra("contentKey")
        val trackKey: String? = intent.getStringExtra("trackKey")

        track_wrapper.visibility = View.GONE
        loader.visibility = View.VISIBLE

        STContents.getInstance()?.getContent(contentKey ?: "", object : STHttpCallback<STContent> {
            override fun onError(t: Throwable) {
                finish()
            }

            override fun onSuccess(data: STContent) {
                track_wrapper.visibility = View.VISIBLE

                content = data
                track = data.elementList?.find { it.key == trackKey }

                val fragmentManager: FragmentManager = supportFragmentManager
                val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
                val fragment = STTrackDetailFragment.withContentAndTrack(content!!, track!!)
                fragmentTransaction.add(
                    R.id.track_container,
                    fragment,
                    "trackFragment"
                )
                fragmentTransaction.commit()

                loader.visibility = View.GONE
            }
        })


    }

}