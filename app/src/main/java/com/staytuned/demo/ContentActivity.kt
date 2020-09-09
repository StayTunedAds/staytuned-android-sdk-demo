package com.staytuned.demo

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.staytuned.demo.adapters.AudioBookDetailAdapter
import com.staytuned.demo.viewmodels.MainViewModel
import com.staytuned.sdk.features.STContents
import com.staytuned.sdk.features.STPlayer
import com.staytuned.sdk.http.STHttpCallback
import com.staytuned.sdk.models.STContent
import com.staytuned.sdk.models.STContentLight
import com.staytuned.sdk.models.lists.STContentList
import com.staytuned.sdk.models.lists.STContentListItem
import com.staytuned.sdk.models.lists.STTrackList
import com.staytuned.sdk.models.lists.STTrackListItem
import kotlinx.android.synthetic.main.activity_content.*

class ContentActivity : AppCompatActivity() {

    private val vModel: MainViewModel by viewModels()
    private var contentLight: STContentLight? = null
    var content: STContent? = null
    set(value) {
        adapter.content = value
        field = value
        updateAdapter()
    }

    var contentList: STContentList? = null
    var trackList: STTrackList? = null
    var adapter = AudioBookDetailAdapter({ content, liked ->
         contentList?.addItems(listOf(STContentListItem().apply { key = content.key; }), object: STHttpCallback<List<STContentListItem>> {
            override fun onSuccess(data: List<STContentListItem>) {
                contentList?.listItems = data.toMutableList()
                updateAdapter()
            }

            override fun onError(t: Throwable) {
                t.printStackTrace()
                Toast.makeText(applicationContext, "Erreur lors du like", Toast.LENGTH_LONG).show()
            }
        })
    }, { track, liked ->
        trackList?.addItems(listOf(STTrackListItem().apply { key = track.key; }), object: STHttpCallback<List<STTrackListItem>> {
            override fun onSuccess(data: List<STTrackListItem>) {
                trackList?.listItems = data.toMutableList()
                updateAdapter()
            }

            override fun onError(t: Throwable) {
                t.printStackTrace()
                Toast.makeText(applicationContext, "Erreur lors du like", Toast.LENGTH_LONG).show()
            }
        })
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content)

        vModel.getLists()
        contentLight = intent.getParcelableExtra("contentLight")
        sdk_staytuned_audio_book_detail_recyclerview.layoutManager = LinearLayoutManager(this)
        sdk_staytuned_audio_book_detail_recyclerview.adapter = adapter

        vModel.myContentsList.observe(this) {
            contentList = it
            adapter.contentList = it
        }
        vModel.myTracksList.observe(this) {
            trackList = it
            adapter.trackList = it
        }

        if (contentLight != null) {
            STContents.getInstance()?.getContent(contentLight!!.key, object : STHttpCallback<STContent> {
                override fun onSuccess(data: STContent) {
                    content = data
                    sdk_staytuned_audio_book_detail_recyclerview.layoutManager = LinearLayoutManager(this@ContentActivity)

                    STPlayer.getInstance()?.currentTrack?.observe(this@ContentActivity, Observer {
                        sdk_staytuned_audio_book_detail_recyclerview.adapter?.notifyDataSetChanged()
                    })
                }

                override fun onError(t: Throwable) {
                    t.printStackTrace()
                }
            })
        } else {
            finish()
        }
    }

    fun updateAdapter() {
        adapter.notifyDataSetChanged()
    }
}