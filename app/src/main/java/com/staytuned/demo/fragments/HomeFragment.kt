package com.staytuned.demo.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.staytuned.demo.R
import com.staytuned.demo.adapters.SectionAdapter
import com.staytuned.demo.viewmodels.MainViewModel
import com.staytuned.sdk.features.STSections
import com.staytuned.sdk.http.STHttpCallback
import com.staytuned.sdk.models.STSection
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment() {

    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        observeData()

    }

    private fun initAdapter() {
        mainRecycler.layoutManager = LinearLayoutManager(requireContext())
        mainRecycler.adapter = SectionAdapter(arrayListOf())
    }

    private fun observeData() {
        mainViewModel.sections.observe(viewLifecycleOwner) {
            (mainRecycler.adapter as SectionAdapter).setSections(it)

            it.forEach { section ->
                getSection(section)
            }
        }
    }

    private fun getSection(section: STSection) {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            STSections.getInstance()?.getSection(section.id, object : STHttpCallback<STSection> {
                override fun onSuccess(data: STSection) {
                    (mainRecycler?.adapter as? SectionAdapter)?.updateSection(data)
                }

                override fun onError(t: Throwable) {
                    Log.e(LOG_TAG, "Error getting sections : ", t)
                }
            })
        }
    }
}

private const val LOG_TAG = "HomeFragment"
