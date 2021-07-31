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
import com.staytuned.demo.EXTRA_CONTENT_LIGHT_KEY
import com.staytuned.sdk.models.STContentLight
import com.staytuned.demo.R
import kotlinx.android.synthetic.main.content_light_item.view.*

class ContentLightAdapter(
    var contentList: List<STContentLight>,
    var layout: Int = R.layout.content_light_item
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val wrapper: LinearLayout = view.content_light_wrapper
        private val image: AppCompatImageView = view.content_light_image
        private val title: TextView = view.content_light_title
        private val author: TextView = view.content_light_author

        fun bind(content: STContentLight) {
            Glide.with(image).clear(image)
            Glide.with(image).applyDefaultRequestOptions(RequestOptions().apply {
                format(DecodeFormat.PREFER_RGB_565)
            }).load(content.imgSrc).centerInside().into(image)

            title.text = content.title
            author.text = content.author

            wrapper.setOnClickListener {
                val intent = Intent(it.context, ContentActivity::class.java)
                intent.putExtra(EXTRA_CONTENT_LIGHT_KEY, content)
                it.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(layout, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int = contentList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val stContentLight = contentList[position]
        (holder as ViewHolder).bind(stContentLight)
    }

    fun setContents(contents: List<STContentLight>) {
        val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                contentList[oldItemPosition].key == contents[newItemPosition].key

            override fun getOldListSize(): Int = contentList.size

            override fun getNewListSize(): Int = contents.size

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                contentList[oldItemPosition].overallDuration == contents[newItemPosition].overallDuration

        }, true)

        result.dispatchUpdatesTo(this)
        contentList = ArrayList(contents)
    }

}
