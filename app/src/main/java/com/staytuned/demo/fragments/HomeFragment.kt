package com.staytuned.demo.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.staytuned.demo.R
import com.staytuned.demo.adapters.SectionAdapter
import com.staytuned.demo.viewmodels.MainViewModel
import com.staytuned.sdk.features.STSections
import com.staytuned.sdk.http.STHttpCallback
import com.staytuned.sdk.models.STSection
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {

    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainRecycler.layoutManager = LinearLayoutManager(requireContext())
        mainRecycler.adapter = SectionAdapter(arrayListOf())

        mainViewModel.sections.observe(viewLifecycleOwner) {
            (mainRecycler.adapter as SectionAdapter).setSections(it)

            it.forEach { section ->
                getSection(section)
            }
        }
    }

    private fun getSection(section: STSection) {
        viewLifecycleOwner.lifecycleScope.launch {
            STSections.getInstance()?.getSection(section.id, object : STHttpCallback<STSection> {
                override fun onSuccess(data: STSection) {
                    (mainRecycler.adapter as SectionAdapter).updateSection(data)
                }

                override fun onError(t: Throwable) {
                    t.printStackTrace()
                }
            })
        }
    }
}
