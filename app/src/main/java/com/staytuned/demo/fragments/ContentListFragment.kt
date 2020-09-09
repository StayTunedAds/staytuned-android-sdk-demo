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
import kotlinx.android.synthetic.main.fragment_content_list.*


class ContentListFragment : Fragment() {

    private val vModel: MainViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_content_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        contentRecycler.layoutManager = GridLayoutManager(requireContext(), 2)
        contentRecycler.adapter = ContentLightAdapter(arrayListOf(), R.layout.appuser_list_item)
        contentRecycler.addItemDecoration(ItemOffsetDecoration(36, 2))

        noContentText.visibility = View.VISIBLE
        contentRecycler.visibility = View.GONE
        vModel.myContentsList.observe(viewLifecycleOwner) {
            if(it == null || it.listItems?.isEmpty() == true) {
                noContentText.visibility = View.VISIBLE
                contentRecycler.visibility = View.GONE
            } else {
                noContentText.visibility = View.GONE
                contentRecycler.visibility = View.VISIBLE
                (contentRecycler.adapter as ContentLightAdapter).setContents(it.listItems?.map { item -> item.item!! } ?: listOf())
            }
        }
    }
 
}