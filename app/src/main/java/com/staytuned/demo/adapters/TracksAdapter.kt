package com.staytuned.demo.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.request.RequestOptions
import com.staytuned.demo.R
import com.staytuned.sdk.features.STPlayer
import com.staytuned.sdk.models.STPlaylist
import com.staytuned.sdk.models.STTrack
import kotlinx.android.synthetic.main.content_light_item.view.*

class TracksAdapter(
    var trackList: List<STTrack>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    inner class TrackViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val wrapper: LinearLayout = view.content_light_wrapper
        private val image: AppCompatImageView = view.content_light_image
        private val title: TextView = view.content_light_title
        private val author: TextView = view.content_light_author

        fun bind(track: STTrack, position: Int) {

            Glide.with(image).clear(image)
            Glide.with(image).applyDefaultRequestOptions(RequestOptions().apply {
                format(DecodeFormat.PREFER_RGB_565)
            }).load(track.imgSrc).centerInside().into(image)

            title.text = track.title
            author.text = track.subtitle

            wrapper.setOnClickListener {
                STPlayer.getInstance()?.play(STPlaylist(ArrayList(trackList)), position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.appuser_list_item, parent, false)
        return TrackViewHolder(view)
    }

    override fun getItemCount(): Int = trackList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentTrack = trackList[position]
        (holder as TrackViewHolder).bind(currentTrack, position)
    }

    fun setContents(tracks: List<STTrack>) {
        val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                trackList[oldItemPosition].key == tracks[newItemPosition].key

            override fun getOldListSize(): Int = trackList.size

            override fun getNewListSize(): Int = tracks.size

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                trackList[oldItemPosition].audioDuration == tracks[newItemPosition].audioDuration

        }, true)

        result.dispatchUpdatesTo(this)
        trackList = ArrayList<STTrack>(tracks)
    }
}
