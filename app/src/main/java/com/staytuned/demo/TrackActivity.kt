package com.staytuned.demo

import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.observe
import com.bumptech.glide.Glide
import com.staytuned.sdk.features.STContents
import com.staytuned.sdk.features.STPlayer
import com.staytuned.sdk.http.STHttpCallback
import com.staytuned.sdk.models.STContent
import com.staytuned.sdk.models.STPlayerState
import com.staytuned.sdk.models.STTrack
import kotlinx.android.synthetic.main.activity_track.*

class TrackActivity : AppCompatActivity() {

    private var content: STContent? = null
    private var track: STTrack? = null

    private var currentTrack: STTrack? = null
    private var currentState: STPlayerState? = null

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
                loader.visibility = View.GONE

                content = data
                track = data.elementList?.find { it.key == trackKey }

                setupView()
            }
        })

        STPlayer.getInstance()?.currentTrack?.observe(this) {
            currentTrack = it
            setupView()
        }
        STPlayer.getInstance()?.currentState?.observe(this) {
            currentState = it
            setupView()
        }

    }

    fun setupView() {
        Glide.with(track_detail_imageview).load(track?.imgSrc).into(track_detail_imageview)

        track_detail_title.text = track?.title
        track_detail_author.text = content?.title

        @Suppress("DEPRECATION")
        track_detail_text.text = Html.fromHtml(track?.descriptionText ?: "")

        play_btn.setOnClickListener {
            if (currentTrack?.key == track?.key) {
                if (currentState == STPlayerState.Playing) {
                    STPlayer.getInstance()?.stop()
                } else {
                    STPlayer.getInstance()?.resume()
                }
            } else {
                track?.let {
                    STPlayer.getInstance()?.play(it)
                }
            }
        }

        if (currentTrack?.key == track?.key) {
            if (currentState == STPlayerState.Playing) {
                play_btn.text = getString(R.string.track_pause)
                play_btn.background = ContextCompat.getDrawable(this, R.drawable.btn_white)
                play_btn.setTextColor(getColor(R.color.colorPrimary))
            } else {
                play_btn.text = getString(R.string.track_resume)
                play_btn.background = ContextCompat.getDrawable(this, R.drawable.btn_primary)
                play_btn.setTextColor(getColor(android.R.color.white))
            }
        } else {
            play_btn.text = getString(R.string.track_start)
            play_btn.background = ContextCompat.getDrawable(this, R.drawable.btn_primary)
            play_btn.setTextColor(getColor(android.R.color.white))
        }
    }
}