package com.staytuned.demo.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.staytuned.demo.R
import com.staytuned.demo.adapters.ContentLightAdapter
import com.staytuned.demo.helpers.ItemOffsetDecoration
import com.staytuned.demo.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.fragment_downloads.*


class DownloadsFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_downloads, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        observeData()
    }

    private fun observeData() {
        with(offlineRecycler) {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = ContentLightAdapter(arrayListOf(), R.layout.appuser_list_item)
            addItemDecoration(ItemOffsetDecoration(36, 2))
        }
    }

    private fun initAdapter() {
        viewModel.offlineList?.observe(viewLifecycleOwner) {
            if (it == null || it.isEmpty()) {
                noContentText.visibility = View.VISIBLE
                offlineRecycler.visibility = View.GONE
            } else {
                noContentText.visibility = View.GONE
                offlineRecycler.visibility = View.VISIBLE
                (offlineRecycler.adapter as ContentLightAdapter)
                    .setContents(it.map { item -> item.audioItem })
            }
        }
    }
}
