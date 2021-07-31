package com.staytuned.demo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.staytuned.demo.R
import com.staytuned.demo.adapters.TracksAdapter
import com.staytuned.demo.helpers.ItemOffsetDecoration
import com.staytuned.demo.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.fragment_track_list.*


class TrackListFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =inflater.inflate(R.layout.fragment_track_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        observeData()
    }

    private fun initAdapter() {
        with(trackRecycler) {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = TracksAdapter(arrayListOf())
            addItemDecoration(ItemOffsetDecoration(36, 2))
            visibility = View.GONE
        }
    }

    private fun observeData() {
        viewModel.myTracksList.observe(viewLifecycleOwner) { trackList ->
            if (trackList.listItems.isNullOrEmpty()) {
                noContentText.visibility = View.VISIBLE
                trackRecycler.visibility = View.GONE
            } else {
                noContentText.visibility = View.GONE
                trackRecycler.visibility = View.VISIBLE
                (trackRecycler.adapter as TracksAdapter).setContents(trackList.listItems.mapNotNull { it.item })
            }
        }
    }
}
