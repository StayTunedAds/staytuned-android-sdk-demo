package com.staytuned.demo

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.staytuned.demo.viewmodels.MainViewModel
import com.staytuned.sdk.features.STContents
import com.staytuned.sdk.http.STHttpCallback
import com.staytuned.sdk.models.STContent
import com.staytuned.sdk.models.STContentLight
import com.staytuned.sdk.ui.STContentDetailFragment
import kotlinx.android.synthetic.main.activity_content.*

class ContentActivity : AppCompatActivity() {

    private val vModel: MainViewModel by viewModels()
    private var contentLight: STContentLight? = null
    var content: STContent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content)

        vModel.getLists()
        contentLight = intent.getParcelableExtra("contentLight")

        /* vModel.myContentsList.observe(this) {
            contentList = it
            adapter.contentList = it
        }
        vModel.myTracksList.observe(this) {
            trackList = it
            adapter.trackList = it
        }
        STOffline.getInstance()?.getTracksObservableByContentKey(contentLight?.key ?: "")
            ?.observe(this) {
                adapter.offlineList = it
            }
        NetworkHelper.connectionLiveData.observe(this) {
            adapter.isOffline = !it.isConnected
        } */

        if (contentLight != null) {
            loader.visibility = View.VISIBLE
            STContents.getInstance()?.getContent(contentLight!!.key, object : STHttpCallback<STContent> {
                override fun onSuccess(data: STContent) {

                    loader.visibility = View.GONE
                    content = data

                    val fragmentManager: FragmentManager = supportFragmentManager
                    val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
                    val fragment = STContentDetailFragment.withContent(data)
                    fragment.onItemDetailClick = { content, track ->
                        val intent = Intent(this@ContentActivity, TrackActivity::class.java)
                        intent.putExtra("contentKey", content.key)
                        intent.putExtra("trackKey", track.key)
                        startActivity(intent)
                    }
                    fragmentTransaction.add(
                        R.id.content_container,
                        fragment,
                        "contentFragment"
                    )
                    fragmentTransaction.commit()
                }

                override fun onError(t: Throwable) {
                    t.printStackTrace()
                }
            })
        } else {
            finish()
        }
    }
}