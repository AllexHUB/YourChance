package com.grigorenko.yourchance.ui.create_new_startup.detail_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.grigorenko.yourchance.databinding.FragmentNameBinding
import com.grigorenko.yourchance.ui.create_new_startup.StartupDetailsViewModel

class NameFragment : Fragment() {
    private var _binding: FragmentNameBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: StartupDetailsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonNavigateUp.setOnClickListener {
            if (validateStartupName()) {
                sharedViewModel.setName(binding.nameStartupField.text.toString())
                findNavController().navigate(NameFragmentDirections.actionNameFragmentToMoneyInvestFragment())
            }
        }
    }

    private fun validateStartupName(): Boolean {
        val nameInput = binding.nameStartupField.text.toString()
        return when {
            nameInput.isEmpty() -> {
                binding.nameStartupContainer.error = "Поле не может быть пустым"
                false
            }
            nameInput.length < 5 -> {
                binding.nameStartupContainer.error = "Название слишком короткое"
                false
            }
            else -> {
                binding.nameStartupContainer.error = null
                true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}