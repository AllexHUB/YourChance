package com.grigorenko.yourchance.ui.startuper.list_of_startups.tablayout.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.grigorenko.yourchance.database.model.Startup
import com.grigorenko.yourchance.database.viewmodel.StartupViewModel
import com.grigorenko.yourchance.databinding.FragmentLatestStartupsBinding
import com.grigorenko.yourchance.ui.startuper.list_of_startups.interfaces.StartupClickListener
import com.grigorenko.yourchance.ui.startuper.list_of_startups.tablayout.adapter.ListOfStartupsAdapter

class LatestStartupsFragment : Fragment(), StartupClickListener {
    private var _binding: FragmentLatestStartupsBinding? = null
    private val binding get() = _binding!!

    private val startupViewModel: StartupViewModel by activityViewModels()

    private lateinit var adapter: ListOfStartupsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLatestStartupsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        startupViewModel.apply {
            getStartupsOrderedByDate()
            userStartups.observe(viewLifecycleOwner) { favStartups ->
                adapter = if (favStartups != null)
                    ListOfStartupsAdapter(this@LatestStartupsFragment, favStartups, viewLifecycleOwner)
                else
                    ListOfStartupsAdapter(this@LatestStartupsFragment, null, viewLifecycleOwner)
                binding.recyclerView.adapter = adapter
                latestStartups.observe(viewLifecycleOwner) { allStartups ->
                    adapter.submitList(allStartups)
                }
            }
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
    }

    override fun onClick(startup: Startup, isFavoriteStartup: Boolean) {
        findNavController().navigate(
            ListOfStartupsFragmentDirections.actionListOfStartupsToSelectedStartup(
                startup, isFavoriteStartup
            )
        )
    }

    override fun onResume() {
        super.onResume()
        binding.recyclerView.smoothScrollToPosition(0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}