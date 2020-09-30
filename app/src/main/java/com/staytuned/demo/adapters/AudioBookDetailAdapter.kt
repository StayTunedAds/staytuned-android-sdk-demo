package com.staytuned.demo.adapters

import android.content.Intent
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.staytuned.demo.R
import com.staytuned.demo.TrackActivity
import com.staytuned.demo.utils.TimeUtil
import com.staytuned.sdk.features.STOffline
import com.staytuned.sdk.features.STPlayer
import com.staytuned.sdk.http.STHttpCallback
import com.staytuned.sdk.models.STContent
import com.staytuned.sdk.models.STTrack
import com.staytuned.sdk.models.lists.STContentList
import com.staytuned.sdk.models.lists.STTrackList
import com.staytuned.sdk.models.offline.STOfflineState
import com.staytuned.sdk.models.offline.STTrackOfflineItem
import kotlinx.android.synthetic.main.content_detail_adapter_header_item_view.view.*
import kotlinx.android.synthetic.main.content_detail_adapter_list_item_view.view.*
import java.time.format.DateTimeFormatter

class AudioBookDetailAdapter(
    private var onContentLiked: (content: STContent, currentLike: Boolean) -> Unit,
    private var onTrackLiked: (track: STTrack, currentLike: Boolean) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var content: STContent? = null
        set(value) {
            value?.elementList?.toMutableList()
                ?.sortWith(compareByDescending<STTrack> { it.chapter }.thenBy { it.chapterPart })
            field = value
            notifyDataSetChanged()
        }
    var offlineList: List<STTrackOfflineItem> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var contentList: STContentList? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var trackList: STTrackList? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var isOffline: Boolean = false
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val audioBookImage: ImageView = view.audio_book_detail_imageview
        val audioBookTitle: TextView = view.audio_book_detail_title
        val audioBookAuthor: TextView = view.audio_book_detail_author
        val audioBookDetailText: TextView = view.audio_book_detail_text
        var audioLike: LinearLayout = view.audio_book_header_like
        var audioLikeImg: AppCompatImageView = view.audio_book_header_like_img
        var audioLikeText: TextView = view.audio_book_header_like_text
    }

    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val audioBookLayout: LinearLayout = view.audio_book_layout
        val audioBookLoader: ProgressBar = view.audio_book_loader
        val audioBookLike: AppCompatImageView = view.audio_book_like
        val audioBookImage: AppCompatImageView = view.audio_book_image
        val audioBookIsDownloaded: AppCompatImageView = view.audio_book_is_downloaded
        val audioBookTitle: TextView = view.audio_book_title
        val audioBookTime: TextView = view.audio_book_time
        val audioBookIsPlaying: TextView = view.audio_book_is_playing
        var audioBookDownload: AppCompatImageView = view.audio_book_download
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return if (viewType == 0) {
            val view =
                inflater.inflate(R.layout.content_detail_adapter_header_item_view, parent, false)

            HeaderViewHolder(view)
        } else {
            val view =
                inflater.inflate(R.layout.content_detail_adapter_list_item_view, parent, false)

            ItemViewHolder(view)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            0
        } else {
            1
        }
    }

    override fun getItemCount(): Int {
        return getList().size + 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position == 0) {
            holder as HeaderViewHolder
            content?.let {
                // Content Like
                holder.audioLike.setOnClickListener { _ ->
                    onContentLiked.invoke(it, isContentLiked())
                }
                if (isContentLiked()) {
                    holder.audioLikeImg.setImageResource(R.drawable.ic_heart_full)
                } else {
                    holder.audioLikeImg.setImageResource(R.drawable.ic_heart)
                }

                Glide.with(holder.audioBookImage).load(it.imgSrc).into(holder.audioBookImage)

                holder.audioBookTitle.text = it.title
                holder.audioBookAuthor.text = it.author

                @Suppress("DEPRECATION")
                holder.audioBookDetailText.text = Html.fromHtml(it.descriptionText)
            }
        } else {
            holder as ItemViewHolder

            val track = getList()[position - 1]

            var isTrackDownloaded = this.isTrackDownloaded(track)
            if (isOffline && !isTrackDownloaded) {
                holder.itemView.alpha = .5f
            } else {
                holder.itemView.alpha = 1f
            }

            holder.audioBookTitle.text = track.title
            holder.audioBookTime.text =
                TimeUtil.milliSecondsToTimerVerbose((track.audioDuration?.toLong() ?: 0L) * 1000)

            holder.audioBookLayout.setOnClickListener {
                val intent = Intent(it.context, TrackActivity::class.java)
                intent.putExtra("contentKey", content?.key)
                intent.putExtra("trackKey", track?.key)
                it.context.startActivity(intent)
            }

            Glide.with(holder.audioBookImage).load(track.imgSrc).into(holder.audioBookImage)

            if (isTrackLiked(track)) {
                holder.audioBookLike.setImageResource(R.drawable.ic_heart_full)
            } else {
                holder.audioBookLike.setImageResource(R.drawable.ic_heart)
            }

            if (isTrackDownloaded) {
                holder.audioBookDownload.setImageResource(R.drawable.ic_delete)
                holder.audioBookIsDownloaded.visibility = View.VISIBLE
            } else {
                holder.audioBookDownload.setImageResource(R.drawable.ic_download)
                holder.audioBookIsDownloaded.visibility = View.GONE
            }

            if (isTrackDownloading(track)) {
                holder.audioBookDownload.visibility = View.GONE
                holder.audioBookLoader.visibility = View.VISIBLE
            } else {
                holder.audioBookDownload.visibility = View.VISIBLE
                holder.audioBookLoader.visibility = View.GONE
            }

            if (STPlayer.getInstance()?.currentTrack?.value?.key == track.key) {
                holder.audioBookIsPlaying.visibility = View.VISIBLE
            } else {
                holder.audioBookIsPlaying.visibility = View.GONE
            }

            holder.audioBookLike.setOnClickListener { _ ->
                onTrackLiked.invoke(track, isTrackLiked(track))
            }

            holder.audioBookDownload.setOnClickListener {
                if (this.isTrackDownloaded(track)) {
                    STOffline.getInstance()?.remove(it.context, track)
                } else {
                    STOffline.getInstance()
                        ?.add(it.context, track, object : STHttpCallback<STTrack> {
                            override fun onSuccess(data: STTrack) {
                                println("Download Success")
                            }

                            override fun onError(t: Throwable) {
                                t.printStackTrace()
                            }
                        })
                }
            }
        }
    }

    private fun getList(): ArrayList<STTrack> {
        return ArrayList(
            (content?.elementList ?: arrayListOf()).filter { it.typeOfElement != "trailer" })
    }

    private fun isContentLiked(): Boolean {
        return false // this.contentList?.listItems?.map { it.item?.key ?: "" }?.any { it == content?.key } == true
    }

    private fun isTrackLiked(track: STTrack): Boolean {
        return false // this.trackList?.listItems?.map { it.item?.key ?: "" }?.any { it == track.key } == true
    }

    private fun isTrackDownloaded(track: STTrack): Boolean {
        return offlineList.any { it.audioItem.key == track.key && it.state == STOfflineState.DOWNLOADED }
    }

    private fun isTrackDownloading(track: STTrack): Boolean {
        return offlineList.any { it.audioItem.key == track.key && it.state != STOfflineState.DOWNLOADED }
    }
}