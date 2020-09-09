package com.staytuned.demo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import com.staytuned.demo.R
import com.staytuned.demo.adapters.TracksAdapter
import com.staytuned.demo.helpers.ItemOffsetDecoration
import com.staytuned.demo.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.fragment_track_list.*


class TrackListFragment : Fragment() {

    private val vModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_track_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        trackRecycler.layoutManager = GridLayoutManager(requireContext(), 2)
        trackRecycler.adapter = TracksAdapter(arrayListOf())
        trackRecycler.addItemDecoration(ItemOffsetDecoration(36, 2))

        noContentText.visibility = View.VISIBLE
        trackRecycler.visibility = View.GONE
        vModel.myTracksList.observe(viewLifecycleOwner) { trackList ->
            if (trackList == null || trackList.listItems?.isEmpty() == true) {
                noContentText.visibility = View.VISIBLE
                trackRecycler.visibility = View.GONE
            } else {
                noContentText.visibility = View.GONE
                trackRecycler.visibility = View.VISIBLE
                (trackRecycler.adapter as TracksAdapter).setContents(trackList.listItems?.map { it.item!! } ?: listOf())
            }
        }
    }

}