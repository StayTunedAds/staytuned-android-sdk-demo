package com.staytuned.demo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.staytuned.demo.viewmodels.MainViewModel
import com.staytuned.sdk.features.STContents
import com.staytuned.sdk.http.STHttpCallback
import com.staytuned.sdk.models.STContent
import com.staytuned.sdk.models.STContentLight
import com.staytuned.sdk.ui.STContentDetailFragment
import com.staytuned.sdk.ui.adapters.STContentDetailTrackHolderStayTunedImpl
import kotlinx.android.synthetic.main.activity_content.*

class ContentActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content)

        mainViewModel.getLists()

        intent.getParcelableExtra<STContentLight>(EXTRA_CONTENT_LIGHT_KEY)?.let { stContentLight ->
            loader.visibility = View.VISIBLE
            STContents.getInstance()?.getContent(stContentLight.key, object : STHttpCallback<STContent> {
                override fun onSuccess(data: STContent) {

                    loader.visibility = View.GONE

                    val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
                    val fragment =
                        STContentDetailFragment.withContent(data, STContentDetailTrackHolderStayTunedImpl(this@ContentActivity) { content, track ->
                            val intent = Intent(this@ContentActivity, TrackActivity::class.java)
                            intent.putExtra(EXTRA_CONTENT_KEY, content.key)
                            intent.putExtra(EXTRA_TRACK_KEY, track.key)
                            startActivity(intent)
                        })
                    fragmentTransaction.add(
                        R.id.content_container,
                        fragment,
                        CONTENT_FRAGMENT_TAG
                    )
                    fragmentTransaction.commit()
                }

                override fun onError(t: Throwable) {
                    Log.e(LOG_TAG, "Error getting content with key ${stContentLight.key}")
                }
            })
        } ?: kotlin.run {
            Log.e(LOG_TAG, "Content is null, finish activity")
            finish()
        }
    }
}

private const val LOG_TAG = "ContentActivity"

private const val CONTENT_FRAGMENT_TAG = "contentFragment"

const val EXTRA_CONTENT_KEY = "contentKey"
const val EXTRA_TRACK_KEY = "trackKey"
private const val EXTRA_CONTENT_LIGHT_KEY = "contentLight"
