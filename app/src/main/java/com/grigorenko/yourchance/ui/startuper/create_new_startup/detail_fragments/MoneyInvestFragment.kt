package com.grigorenko.yourchance.ui.startuper.create_new_startup.detail_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.grigorenko.yourchance.database.model.MoneyInvest
import com.grigorenko.yourchance.databinding.FragmentMoneyInvestBinding
import com.grigorenko.yourchance.ui.startuper.create_new_startup.StartupDetailsViewModel

class MoneyInvestFragment : Fragment() {
    private var _binding: FragmentMoneyInvestBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: StartupDetailsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoneyInvestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonNavigateUp.setOnClickListener {
            if(validateStartupMoneyForInvest()) {
                sharedViewModel.setMoneyInvest(MoneyInvest(binding.moneyInvestField.text.toString().toLong(), 0))
                findNavController().navigate(MoneyInvestFragmentDirections.actionMoneyInvestFragmentToDescriptionFragment())
            }
        }
    }

    private fun validateStartupMoneyForInvest(): Boolean {
        val moneyInvestInput = binding.moneyInvestField.text.toString()
        return when {
            moneyInvestInput.isEmpty() -> {
                binding.moneyInvestContainer.error = "Поле не может быть пустым"
                false
            }
            moneyInvestInput.toDouble() == 0.0 || moneyInvestInput[0] == '0' -> {
                binding.moneyInvestContainer.error = "Сумма для инвестиций не может быть нулевой"
                false
            }
            else -> {
                binding.moneyInvestContainer.error = null
                true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}