package com.grigorenko.yourchance.ui.startuper.list_of_startups.tablayout.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.grigorenko.yourchance.R
import com.grigorenko.yourchance.databinding.FragmentListOfStartupsBinding
import com.grigorenko.yourchance.ui.startuper.list_of_startups.tablayout.adapter.ViewPagerAdapter


class ListOfStartupsFragment : Fragment() {
    private var _binding: FragmentListOfStartupsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListOfStartupsBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val tabLayout = binding.tabLayout
        val viewPager = binding.viewPager
        val viewPagerAdapter = ViewPagerAdapter(childFragmentManager, lifecycle)

        viewPager.adapter = viewPagerAdapter
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Популярные"
                1 -> tab.text = "Новые"
            }
        }.attach()
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.sorting_menu, menu)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}