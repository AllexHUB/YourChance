package com.grigorenko.yourchance.ui.startuper.my_startups.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.grigorenko.yourchance.R
import com.grigorenko.yourchance.databinding.FragmentEditStartupBinding
import com.grigorenko.yourchance.domain.model.Image
import com.grigorenko.yourchance.domain.model.Location
import com.grigorenko.yourchance.domain.model.MoneyInvest
import com.grigorenko.yourchance.domain.viewmodel.StartupViewModel
import com.grigorenko.yourchance.ui.startuper.create_new_startup.StartupDetailsViewModel
import com.squareup.picasso.Picasso

class EditStartupFragment : Fragment() {
    private var _binding: FragmentEditStartupBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: StartupDetailsViewModel by activityViewModels()
    private val startupViewModel: StartupViewModel by activityViewModels()

    private val args: EditStartupFragmentArgs by navArgs()

    private var finalImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditStartupBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            Picasso.get()
                .load(args.startup.image.uri.toUri())
                .fit().centerCrop()
                .into(binding.startupImage)
            nameStartupField.setText(args.startup.name)
            countryStartupField.setText(args.startup.location.country)
            cityStartupField.setText(args.startup.location.city)
            descriptionStartupField.setText(args.startup.description)
            moneyInvestField.setText(args.startup.moneyInvest.wholeSum.toString())
            moneyCollectedField.setText(args.startup.moneyInvest.collectedSum.toString())
            buttonCategorySelect.text = args.startup.category
            buttonCategorySelect.setOnClickListener {
                val popUpMenu = PopupMenu(context, it)
                popUpMenu.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.business -> {
                            buttonCategorySelect.text = resources.getString(R.string.business)
                            true
                        }
                        R.id.food -> {
                            buttonCategorySelect.text = resources.getString(R.string.food)
                            true
                        }
                        R.id.art -> {
                            buttonCategorySelect.text = resources.getString(R.string.art)
                            true
                        }
                        R.id.society -> {
                            buttonCategorySelect.text = resources.getString(R.string.society)
                            true
                        }
                        R.id.it -> {
                            buttonCategorySelect.text = resources.getString(R.string.it)
                            true
                        }
                        R.id.technology -> {
                            buttonCategorySelect.text = resources.getString(R.string.technology)
                            true
                        }
                        R.id.trip -> {
                            buttonCategorySelect.text = resources.getString(R.string.trip)
                            true
                        }
                        R.id.sport -> {
                            buttonCategorySelect.text = resources.getString(R.string.sport)
                            true
                        }
                        else -> false
                    }
                }
                popUpMenu.inflate(R.menu.category_list_menu)
                popUpMenu.show()
            }
            startupImage.setOnClickListener {
                openFileChooser()
            }
            buttonConfirmChanges.setOnClickListener {
                if (validateStartupName() &&
                    validateStartupDescription() &&
                    validateStartupCountry() &&
                    validateStartupCity() &&
                    validateStartupMoneyForInvest() &&
                    validateStartupMoneyCollected()) {

                    val drawable: Drawable? = if (!validateFinalImageContent()) {
                        sharedViewModel.setImage(Image(args.startup.image.name, args.startup.image.uri))
                        null
                    } else
                        binding.startupImage.drawable

                    sharedViewModel.apply {
                        setName(binding.nameStartupField.text.toString())
                        setLocation(
                            Location(
                                binding.countryStartupField.text.toString(),
                                binding.cityStartupField.text.toString()
                            )
                        )
                        setDescription(binding.descriptionStartupField.text.toString())
                        setMoneyInvest(
                            MoneyInvest(
                                binding.moneyInvestField.text.toString().toLong(),
                                binding.moneyCollectedField.text.toString().toLong()
                            )
                        )
                        setCategory(binding.buttonCategorySelect.text.toString())
                        setViews(args.startup.views)
                        setOwnerId(args.startup.ownerId)
                    }
                    val updatedStartup = sharedViewModel.makeNewStartup()
                    startupViewModel.apply {
                        updateUserStartup(
                            drawable,
                            updatedStartup,
                            Image(args.startup.image.name, args.startup.image.uri)
                        )
                        isStartupUpdated.observe(viewLifecycleOwner) {
                            if (it)
                                findNavController().navigate(EditStartupFragmentDirections.actionEditStartupToMyStartups())
                        }
                    }
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
            if (it.resultCode == Activity.RESULT_OK && it.data != null) {
                finalImageUri = it.data!!.data
                sharedViewModel.setImage(Image(args.startup.image.name, it.data!!.data.toString()))
                Picasso.get()
                    .load(it.data!!.data)
                    .fit().centerCrop()
                    .into(binding.startupImage)
            }
        }

    private fun validateFinalImageContent(): Boolean {
        return finalImageUri != null
    }

    private fun validateStartupCountry(): Boolean {
        val countryInput = binding.countryStartupField.text.toString()
        return when {
            countryInput.isEmpty() -> {
                binding.countryStartupContainer.error = "Поле не может быть пустым"
                false
            }
            countryInput.isDigitsOnly() -> {
                binding.countryStartupContainer.error = "В названии страны не могут быть цифры"
                false
            }
            else -> {
                binding.countryStartupContainer.error = null
                true
            }
        }
    }

    private fun validateStartupCity(): Boolean {
        val cityInput = binding.cityStartupField.text.toString()
        return when {
            cityInput.isEmpty() -> {
                binding.cityStartupContainer.error = "Поле не может быть пустым"
                false
            }
            cityInput.isDigitsOnly() -> {
                binding.cityStartupContainer.error = "В названии города не могут быть цифры"
                false
            }
            else -> {
                binding.cityStartupContainer.error = null
                true
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

    private fun validateStartupMoneyForInvest(): Boolean {
        val moneyInvestInput = binding.moneyInvestField.text.toString()
        return when {
            moneyInvestInput.isEmpty() -> {
                binding.moneyInvestContainer.error = "Поле не может быть пустым"
                false
            }
            moneyInvestInput.toLong() == 0L -> {
                binding.moneyInvestContainer.error = "Сумма для инвестиций не может быть нулевой"
                false
            }
            else -> {
                binding.moneyInvestContainer.error = null
                true
            }
        }
    }

    private fun validateStartupMoneyCollected(): Boolean {
        val moneyInvestInput = binding.moneyInvestField.text.toString()
        val moneyCollectedInput = binding.moneyCollectedField.text.toString()
        return when {
            moneyCollectedInput.isEmpty() -> {
                binding.moneyCollectedContainer.error = "Поле не может быть пустым"
                false
            }
            moneyCollectedInput.toLong() >= moneyInvestInput.toLong() -> {
                binding.moneyCollectedContainer.error = "Собранная сумма должна быть меньше необходимой"
                false
            }
            else -> {
                binding.moneyCollectedContainer.error = null
                true
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