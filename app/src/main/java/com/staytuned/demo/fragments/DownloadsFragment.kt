package com.staytuned.demo.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import com.staytuned.demo.R
import com.staytuned.demo.adapters.ContentLightAdapter
import com.staytuned.demo.helpers.ItemOffsetDecoration
import com.staytuned.demo.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.fragment_downloads.*


class DownloadsFragment : Fragment() {

    private val vModel: MainViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_downloads, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        offlineRecycler.layoutManager = GridLayoutManager(requireContext(), 2)
        offlineRecycler.adapter = ContentLightAdapter(arrayListOf(), R.layout.appuser_list_item)
        offlineRecycler.addItemDecoration(ItemOffsetDecoration(36, 2))

        noContentText.visibility = View.VISIBLE
        offlineRecycler.visibility = View.GONE
        vModel.offlineList?.observe(viewLifecycleOwner) {
            if (it == null || it.isEmpty()) {
                noContentText.visibility = View.VISIBLE
                offlineRecycler.visibility = View.GONE
            } else {
                noContentText.visibility = View.GONE
                offlineRecycler.visibility = View.VISIBLE
                (offlineRecycler.adapter as ContentLightAdapter)
                    .setContents(it?.map { item -> item.audioItem } ?: listOf())
            }
        }
    }

}