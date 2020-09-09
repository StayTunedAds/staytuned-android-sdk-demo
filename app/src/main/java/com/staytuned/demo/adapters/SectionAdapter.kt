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


    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var sectionsRecyclerView: RecyclerView = v.section_recyclerview
        var homeCategoryTitle: TextView = v.section_title
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.section_item_view, parent, false)

        v.section_recyclerview.layoutManager = LinearLayoutManager(parent.context, LinearLayoutManager.HORIZONTAL, false)
        v.section_recyclerview.adapter = ContentLightAdapter(listOf())

        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return sectionList.size
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentCategory = sectionList[position]
        bindListItem(holder as ViewHolder, currentCategory, position)
    }

    fun setSections(sections: List<STSection>) {
        val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return sectionList[oldItemPosition].id == sections[newItemPosition].id
            }

            override fun getOldListSize(): Int {
                return sectionList.size
            }

            override fun getNewListSize(): Int {
                return sections.size
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return sectionList[oldItemPosition].linkedContents?.size == sections[newItemPosition].linkedContents?.size
            }

        }, true);
        result.dispatchUpdatesTo(this)

        val cloneList = ArrayList<STSection>()
        cloneList.addAll(sections)
        sectionList = cloneList
    }

    fun updateSection(section: STSection) {
        val foundSectionIndex = sectionList.indexOfFirst { it.id == section.id }

        if(foundSectionIndex >= 0) {
            sectionList[foundSectionIndex] = section
            notifyItemChanged(foundSectionIndex)
        }
    }

    private fun bindListItem(
        holder: ViewHolder,
        section: STSection,
        position: Int
    ) {

        val adapter = holder.sectionsRecyclerView.adapter as ContentLightAdapter
        adapter.setContents(section.linkedContents ?: listOf())
        holder.homeCategoryTitle.text = section.name

        holder.itemView.setOnClickListener {  }
    }

}