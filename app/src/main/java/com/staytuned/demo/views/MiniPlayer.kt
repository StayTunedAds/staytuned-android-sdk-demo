package com.staytuned.demo.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.staytuned.demo.R
import com.staytuned.sdk.features.STPlayer
import kotlinx.android.synthetic.main.mini_player.view.*

class MiniPlayer(context: Context, attrs: AttributeSet? = null) : LinearLayout(context, attrs) {

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.mini_player, this, true)

        mini_player.setOnClickListener {
            STPlayer.getInstance()?.launchUI(it.context)
        }

        mini_player_play_toggle.setOnClickListener {
            togglePlayPause()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        STPlayer.getInstance()?.currentTrack?.observe(context as LifecycleOwner, Observer {
            visibility = if (it == null) {
                View.GONE
            } else {
                View.VISIBLE
            }

            mini_player_track_title.text = it?.title
            Glide.with(this).load(it?.imgSrc).centerInside().into(mini_player_image)
        })
        STPlayer.getInstance()?.currentContent?.observe(context as LifecycleOwner, Observer {
            mini_player_content_title.text = it?.title
        })
        STPlayer.getInstance()?.currentTime?.observe(context as LifecycleOwner, Observer {
            mini_player_progress.progress = STPlayer.getInstance()?.getAudioProgress()?.toInt() ?: 0
        })
        STPlayer.getInstance()?.currentState?.observe(context as LifecycleOwner, Observer {
            setVisualizerState()
            setButtonState()
        })
    }

    private fun setVisualizerState() {
        // pause or play the visualizer animation by the player state
        mini_player_visualizer.isPlaying = STPlayer.getInstance()?.isPlaying() == true
    }

    private fun setButtonState() {
        val player = STPlayer.getInstance()
        when {
            // When it's playing, we show a pause button and hide the loader
            player?.isPlaying() == true && !player.isLoading() -> {
                mini_player_play_toggle.visibility = View.VISIBLE
                mini_player_play_loader.visibility = View.GONE
                mini_player_play_toggle.setImageResource(com.staytuned.R.drawable.st_ic_pause)
            }
            // When it's loading, we hide the button and show the loader
            player?.isLoading() == true -> {
                mini_player_play_toggle.visibility = View.GONE
                mini_player_play_loader.visibility = View.VISIBLE
            }
            // Else we consider the state as PAUSED, we show a play icon and hide the loader
            else -> {
                mini_player_play_toggle.visibility = View.VISIBLE
                mini_player_play_loader.visibility = View.GONE
                mini_player_play_toggle.setImageResource(com.staytuned.R.drawable.st_ic_play)
            }
        }
    }

    private fun togglePlayPause() {
        if (STPlayer.getInstance()?.isPlaying() == true) {
            STPlayer.getInstance()?.stop()
        } else {
            STPlayer.getInstance()?.resume()
        }
    }
}