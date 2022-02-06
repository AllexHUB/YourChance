package com.grigorenko.yourchance.ui.my_startup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.grigorenko.yourchance.databinding.FragmentMyStartupBinding

class MyStartupFragment : Fragment() {

    private lateinit var myStartupViewModel: MyStartupViewModel
    private var _binding: FragmentMyStartupBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        myStartupViewModel =
            ViewModelProvider(this).get(MyStartupViewModel::class.java)

        _binding = FragmentMyStartupBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textSlideshow
        myStartupViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}