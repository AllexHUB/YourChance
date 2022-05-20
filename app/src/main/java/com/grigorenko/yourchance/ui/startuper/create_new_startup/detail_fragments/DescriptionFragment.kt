package com.grigorenko.yourchance.ui.startuper.create_new_startup.detail_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.grigorenko.yourchance.databinding.FragmentDescriptionBinding
import com.grigorenko.yourchance.ui.startuper.create_new_startup.StartupDetailsViewModel

class DescriptionFragment : Fragment() {
    private var _binding: FragmentDescriptionBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel : StartupDetailsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDescriptionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonNavigateUp.setOnClickListener {
            if (validateStartupDescription()) {
                sharedViewModel.setDescription(binding.descriptionStartupField.text.toString())
                findNavController().navigate(DescriptionFragmentDirections.actionDescriptionFragmentToLocationFragment())
            }
        }
    }

    private fun validateStartupDescription(): Boolean {
        val descriptionInput = binding.descriptionStartupField.text.toString()
        return when {
            descriptionInput.isEmpty() -> {
                binding.descriptionStartupContainer.error = "Поле не может быть пустым"
                false
            }
            descriptionInput.length < 5 -> {
                binding.descriptionStartupContainer.error = "Описание слишком короткое"
                false
            }
            else -> {
                binding.descriptionStartupContainer.error = null
                true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}