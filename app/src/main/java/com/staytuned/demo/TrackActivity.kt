package com.staytuned.demo

import android.os.Bundle
import android.text.Html
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.staytuned.demo.adapters.AudioBookDetailAdapter
import com.staytuned.demo.viewmodels.MainViewModel
import com.staytuned.sdk.features.STContents
import com.staytuned.sdk.features.STPlayer
import com.staytuned.sdk.http.STHttpCallback
import com.staytuned.sdk.models.STContent
import com.staytuned.sdk.models.STContentLight
import com.staytuned.sdk.models.STTrack
import com.staytuned.sdk.models.lists.STContentList
import com.staytuned.sdk.models.lists.STContentListItem
import com.staytuned.sdk.models.lists.STTrackList
import com.staytuned.sdk.models.lists.STTrackListItem
import kotlinx.android.synthetic.main.activity_content.*
import kotlinx.android.synthetic.main.activity_track.*

class TrackActivity : AppCompatActivity() {

    private var content: STContent? = null
    private var track: STTrack? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_track)

        val contentKey: String? = intent.getStringExtra("contentKey")
        val trackKey: String? = intent.getStringExtra("trackKey")

        STContents.getInstance()?.getContent(contentKey ?: "", object: STHttpCallback<STContent> {
            override fun onError(t: Throwable) {
                finish()
            }

            override fun onSuccess(data: STContent) {
                content = data
                track = data.elementList?.find { it.key == trackKey }

                setupView()
            }
        })

    }

    fun setupView() {
        Glide.with(track_detail_imageview).load(track?.imgSrc).into(track_detail_imageview)

        track_detail_title.text = track?.title
        track_detail_author.text = content?.title

        @Suppress("DEPRECATION")
        track_detail_text.text = Html.fromHtml(track?.descriptionText)
    }
}