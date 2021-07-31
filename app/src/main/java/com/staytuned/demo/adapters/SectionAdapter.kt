package com.staytuned.demo.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.staytuned.sdk.models.STSection
import com.staytuned.demo.R
import kotlinx.android.synthetic.main.section_item_view.view.*

class SectionAdapter(
    var sectionList: ArrayList<STSection>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var sectionsRecyclerView: RecyclerView = view.section_recyclerview
        var homeCategoryTitle: TextView = view.section_title

        fun bind(section: STSection) {
            (sectionsRecyclerView.adapter as? ContentLightAdapter)?.setContents(section.linkedContents ?: listOf())
            homeCategoryTitle.text = section.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.section_item_view, parent, false)

        view.section_recyclerview.layoutManager = LinearLayoutManager(parent.context, LinearLayoutManager.HORIZONTAL, false)
        view.section_recyclerview.adapter = ContentLightAdapter(listOf())

        return ViewHolder(view)
    }

    override fun getItemCount(): Int = sectionList.size


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val section = sectionList[position]
        (holder as ViewHolder).bind(section)
    }

    fun setSections(sections: List<STSection>) {
        val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                sectionList[oldItemPosition].id == sections[newItemPosition].id

            override fun getOldListSize(): Int = sectionList.size

            override fun getNewListSize(): Int = sections.size

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                sectionList[oldItemPosition].linkedContents?.size == sections[newItemPosition].linkedContents?.size

        }, true)
        result.dispatchUpdatesTo(this)

        sectionList = ArrayList(sections)
    }

    fun updateSection(section: STSection) {
        val foundSectionIndex = sectionList.indexOfFirst { it.id == section.id }

        if(foundSectionIndex >= 0) {
            sectionList[foundSectionIndex] = section
            notifyItemChanged(foundSectionIndex)
        }
    }
}
