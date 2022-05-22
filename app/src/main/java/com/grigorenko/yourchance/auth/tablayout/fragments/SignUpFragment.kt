package com.grigorenko.yourchance.auth.tablayout.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.grigorenko.yourchance.R
import com.grigorenko.yourchance.databinding.FragmentSignUpBinding
import com.grigorenko.yourchance.domain.model.Image
import com.grigorenko.yourchance.domain.model.User
import com.grigorenko.yourchance.domain.viewmodel.AuthViewModel
import com.grigorenko.yourchance.domain.viewmodel.UserViewModel
import com.grigorenko.yourchance.ui.MainActivity
import java.util.regex.Pattern

class SignUpFragment : Fragment() {
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    private val authViewModel: AuthViewModel by activityViewModels()
    private val userViewModel: UserViewModel by activityViewModels()

    private var userType = "Startuper"

    private val passwordPattern = Pattern.compile(
        "^" + "(?=.*[0-9])" +     //at least 1 digit
                "(?=.*[a-z])" +         //at least 1 lower case letter
                "(?=.*[A-Z])" +         //at least 1 upper case letter
                // "(?=.*[a-zA-Z])" +   //any letter
                "(?=\\S+$)" +           //no white spaces
                ".{8,}" +               //at least 8 characters
                "$"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authViewModel.firebaseUser.observe(viewLifecycleOwner) {
            if (it != null) {
                val user = User(
                    binding.emailField.text.toString(),
                    binding.nameField.text.toString(),
                    binding.phoneField.text.toString(),
                    listOf(),
                    Image(),
                    userType
                )
                userViewModel.addNewUser(it.uid, user)
                updateUi(user)
            }
        }

        binding.apply {
            signUpButton.setOnClickListener {
                if (validateName() and validateEmail() and validatePassword() and validatePhoneNumber())
                    authViewModel.signUpWithEmail(
                        this.emailField.text.toString(),
                        this.passwordField.text.toString()
                    )
            }
            radioGroup.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.startuper_rb -> {
                        userType = "Startuper"
                    }
                    R.id.investor_rb -> {
                        userType = "Investor"
                    }
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        binding.apply {
            nameContainer.error = null
            nameField.text?.clear()
            emailField.text?.clear()
            emailContainer.error = null
            passwordField.text?.clear()
            passwordContainer.error = null
            phoneField.text?.clear()
            phoneContainer.error = null
        }
    }

    private fun updateUi(user: User?) {
        if (user != null) {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra("user", user)
            startActivity(intent)
            activity?.finish()
        } else {
            Log.e("AUTH", "User is null")
        }
    }

    private fun validateName(): Boolean {
        val nameInput = binding.nameField.text.toString()
        return when {
            nameInput.isEmpty() -> {
                binding.nameContainer.error = "Поле не может быть пустым"
                false
            }
            nameInput.length < 10 -> {
                binding.nameContainer.error = "Введите Ваше имя полностью"
                false
            }
            else -> {
                binding.nameContainer.error = null
                true
            }
        }
    }

    private fun validateEmail(): Boolean {
        val emailInput = binding.emailField.text.toString()
        return when {
            emailInput.isEmpty() -> {
                binding.emailContainer.error = "Поле не может быть пустым"
                false
            }
            !Patterns.EMAIL_ADDRESS.matcher(emailInput).matches() -> {
                binding.emailContainer.error = "Введите правильный email адрес"
                false
            }
            else -> {
                binding.emailContainer.error = null
                true
            }
        }
    }

    private fun validatePassword(): Boolean {
        val passwordInput = binding.passwordField.text.toString()
        return when {
            passwordInput.isEmpty() -> {
                binding.passwordContainer.error = "Поле не может быть пустым"
                false
            }
            !passwordPattern.matcher(passwordInput).matches() -> {
                binding.passwordContainer.error = "Пароль слишком легкий"
                false
            }
            else -> {
                binding.passwordContainer.error = null
                true
            }
        }
    }

    private fun validatePhoneNumber(): Boolean {
        val phoneNumberInput = binding.phoneField.text.toString()
        return when {
            phoneNumberInput.isEmpty() -> {
                binding.phoneContainer.error = "Поле не может быть пустым"
                false
            }
            phoneNumberInput.first() != '0' -> {
                binding.phoneContainer.error = "Номер должен начинаться с нуля"
                false
            }
            phoneNumberInput.length != 10 -> {
                binding.phoneContainer.error = "Номер указан не полностью"
                false
            }
            else -> {
                binding.phoneContainer.error = null
                true
            }
        }
    }
}
