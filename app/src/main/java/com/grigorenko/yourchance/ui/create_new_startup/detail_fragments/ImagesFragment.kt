package com.grigorenko.yourchance.ui.create_new_startup.detail_fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.Timestamp
import com.grigorenko.yourchance.databinding.FragmentImagesBinding
import com.grigorenko.yourchance.database.model.Image
import com.grigorenko.yourchance.ui.create_new_startup.StartupDetailsViewModel
import com.grigorenko.yourchance.database.viewmodel.StartupViewModel
import com.grigorenko.yourchance.database.viewmodel.UserViewModel
import com.squareup.picasso.Picasso
import java.util.*

class ImagesFragment : Fragment() {
    private var _binding: FragmentImagesBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: StartupDetailsViewModel by activityViewModels()
    private lateinit var startupViewModel: StartupViewModel
    private lateinit var userViewModel: UserViewModel

    private lateinit var startImageContent: Drawable

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        startupViewModel = ViewModelProvider(this)[StartupViewModel::class.java]
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        _binding = FragmentImagesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startImageContent = binding.startupImage.drawable
        binding.apply {
            startupImage.setOnClickListener {
                openFileChooser()
            }
            buttonNavigateUp.setOnClickListener {
                if (validateImageContent()) {
                    sharedViewModel.setDate(Timestamp(Date()))
                    val newStartup = sharedViewModel.makeNewStartup()
                    val userUid = userViewModel.getCurrentUserUID()
                    startupViewModel.addNewStartupToFirestore(binding.startupImage.drawable, newStartup, userUid)

                    findNavController().navigate(ImagesFragmentDirections.actionImagesFragmentToMyStartupFragment())
                }
            }
        }
    }

    private fun openFileChooser() {
        val intent = Intent().setType("image/*").setAction(Intent.ACTION_GET_CONTENT)
        activityResult.launch(intent)
    }

    private val activityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK && it.data != null) {
                val generatedName = System.currentTimeMillis().toString()
                sharedViewModel.setImage(Image(generatedName, null.toString()))
                Picasso.get()
                    .load(it.data!!.data)
                    .fit().centerCrop()
                    .into(binding.startupImage)
            }
        }

    private fun validateImageContent(): Boolean {
        val content = binding.startupImage.drawable
        return if (content == startImageContent) {
            Toast.makeText(context, "Выберите изображение для стартапа!!", Toast.LENGTH_SHORT)
                .show()
            false
        } else
            true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}