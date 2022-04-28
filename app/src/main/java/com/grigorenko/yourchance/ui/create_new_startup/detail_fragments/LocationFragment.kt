package com.grigorenko.yourchance.ui.create_new_startup.detail_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.grigorenko.yourchance.database.model.Location
import com.grigorenko.yourchance.databinding.FragmentLocationBinding
import com.grigorenko.yourchance.ui.create_new_startup.StartupDetailsViewModel

class LocationFragment : Fragment() {
    private var _binding: FragmentLocationBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: StartupDetailsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLocationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.buttonNavigateUp.setOnClickListener {
            sharedViewModel.setLocation(Location(binding.countryStartupField.text.toString(), binding.cityStartupField.text.toString()))
            findNavController().navigate(LocationFragmentDirections.actionLocationFragmentToImagesFragment())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}