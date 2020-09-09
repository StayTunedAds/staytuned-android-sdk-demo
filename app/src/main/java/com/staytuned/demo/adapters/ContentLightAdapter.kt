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
import kotlinx.android.synthetic.main.content_light_item.view.*

class ContentLightAdapter(
    var contentList: List<STContentLight>,
    var layout: Int = R.layout.content_light_item
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val wrapper: LinearLayout = v.content_light_wrapper
        val image: AppCompatImageView = v.content_light_image
        val title: TextView = v.content_light_title
        val author: TextView = v.content_light_author
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(layout, parent, false)

        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return contentList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentCategory = contentList[position]
        bindListItem(holder as ViewHolder, currentCategory)
    }

    fun setContents(contents: List<STContentLight>) {
        val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return contentList[oldItemPosition].key == contents[newItemPosition].key
            }

            override fun getOldListSize(): Int {
                return contentList.size
            }

            override fun getNewListSize(): Int {
                return contents.size
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return contentList[oldItemPosition].overallDuration == contents[newItemPosition].overallDuration
            }

        }, true)
        result.dispatchUpdatesTo(this)

        val cloneList = ArrayList<STContentLight>()
        cloneList.addAll(contents)
        contentList = cloneList
    }

    private fun bindListItem(
        holder: ViewHolder,
        content: STContentLight
    ) {
        Glide.with(holder.image).clear(holder.image)
        Glide.with(holder.image).applyDefaultRequestOptions(RequestOptions().apply {
            format(DecodeFormat.PREFER_RGB_565)
        }).load(content.imgSrc).centerInside().into(holder.image)

        holder.title.text = content.title
        holder.author.text = content.author

        holder.wrapper.setOnClickListener {
            val intent = Intent(it.context, ContentActivity::class.java)
            intent.putExtra("contentLight", content)
            it.context.startActivity(intent)
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        holder as ViewHolder
    }
}