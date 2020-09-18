package com.staytuned.demo.adapters

import android.content.Intent
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
import com.staytuned.demo.ContentActivity
import com.staytuned.sdk.models.STContent
import com.staytuned.sdk.models.STContentLight
import com.staytuned.demo.R
import com.staytuned.sdk.features.STPlayer
import com.staytuned.sdk.features.STSections
import com.staytuned.sdk.http.STHttpCallback
import com.staytuned.sdk.models.STPlaylist
import com.staytuned.sdk.models.STTrack
import kotlinx.android.synthetic.main.content_light_item.view.*

class TracksAdapter(
    var trackList: List<STTrack>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val wrapper: LinearLayout = v.content_light_wrapper
        val image: AppCompatImageView = v.content_light_image
        val title: TextView = v.content_light_title
        val author: TextView = v.content_light_author
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.appuser_list_item, parent, false)

        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return trackList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentCategory = trackList[position]
        bindListItem(holder as ViewHolder, currentCategory, position)
    }

    fun setContents(tracks: List<STTrack>) {
        val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return trackList[oldItemPosition].key == tracks[newItemPosition].key
            }

            override fun getOldListSize(): Int {
                return trackList.size
            }

            override fun getNewListSize(): Int {
                return tracks.size
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return trackList[oldItemPosition].audioDuration == tracks[newItemPosition].audioDuration
            }

        }, true)
        result.dispatchUpdatesTo(this)

        val cloneList = ArrayList<STTrack>()
        cloneList.addAll(tracks)
        trackList = cloneList
    }

    private fun bindListItem(holder: ViewHolder, track: STTrack, position: Int) {
        Glide.with(holder.image).clear(holder.image)
        Glide.with(holder.image).applyDefaultRequestOptions(RequestOptions().apply {
            format(DecodeFormat.PREFER_RGB_565)
        }).load(track.imgSrc).centerInside().into(holder.image)

        holder.title.text = track.title
        holder.author.text = track.subtitle

        holder.wrapper.setOnClickListener {
            STPlayer.getInstance()?.play(STPlaylist(ArrayList(trackList)), position)
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        holder as ViewHolder
    }
}