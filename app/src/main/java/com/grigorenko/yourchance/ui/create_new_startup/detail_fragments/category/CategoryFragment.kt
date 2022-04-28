package com.grigorenko.yourchance.ui.create_new_startup.detail_fragments.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.grigorenko.yourchance.databinding.FragmentCategoryBinding
import com.grigorenko.yourchance.ui.create_new_startup.StartupDetailsViewModel

class CategoryFragment : Fragment(), CategoryClickListener {
    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var  categoryViewModel: CategoryViewModel
    private val sharedVIewModel: StartupDetailsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        categoryViewModel = ViewModelProvider(this).get(CategoryViewModel::class.java)
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.categoryRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = CategoryAdapter(categoryViewModel.insertCategoryList(), this@CategoryFragment)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(category: Category) {
        sharedVIewModel.setCategory(category.name)
        findNavController().navigate(CategoryFragmentDirections.actionCategoryFragmentToNameFragment())
        categoryViewModel.clearCategoryList()
    }
}