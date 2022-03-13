package com.grigorenko.yourchance.ui.list_of_startups

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.grigorenko.yourchance.databinding.FragmentListOfStartupsBinding

class ListOfStartupsFragment : Fragment() {

    private lateinit var listOfStartupsViewModel: ListOfStartupsViewModel
    private var _binding: FragmentListOfStartupsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        listOfStartupsViewModel =
            ViewModelProvider(this)[ListOfStartupsViewModel::class.java]

        _binding = FragmentListOfStartupsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        listOfStartupsViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}