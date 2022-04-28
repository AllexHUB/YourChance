package com.grigorenko.yourchance.ui.list_of_startups.tablayout.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.grigorenko.yourchance.database.model.Startup
import com.grigorenko.yourchance.databinding.FragmentLatestStartupsBinding
import com.grigorenko.yourchance.database.viewmodel.StartupViewModel
import com.grigorenko.yourchance.ui.list_of_startups.ListOfStartupsAdapter
import com.grigorenko.yourchance.ui.list_of_startups.StartupClickListener
import com.grigorenko.yourchance.ui.list_of_startups.tablayout.ListOfStartupsFragmentDirections

class LatestStartupsFragment : Fragment(), StartupClickListener {
    private var _binding: FragmentLatestStartupsBinding? = null
    private val binding get() = _binding!!

    private val startupViewModel: StartupViewModel by activityViewModels()

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
            latestStartups.observe(viewLifecycleOwner) {
                if (it != null) {
                    binding.recyclerView.layoutManager = LinearLayoutManager(context)
                    binding.recyclerView.adapter = ListOfStartupsAdapter(it, this@LatestStartupsFragment)
                } else
                    binding.recyclerView.layoutManager = null
            }
        }
    }

    override fun onClick(startup: Startup) {
        findNavController().navigate(ListOfStartupsFragmentDirections.actionNavListOfStartupsToSelectedStartupFragment(startup))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}