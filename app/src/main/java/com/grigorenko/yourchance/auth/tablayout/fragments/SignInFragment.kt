package com.grigorenko.yourchance.auth.tablayout.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseUser
import com.grigorenko.yourchance.database.model.Image
import com.grigorenko.yourchance.database.model.User
import com.grigorenko.yourchance.database.viewmodel.AuthViewModel
import com.grigorenko.yourchance.database.viewmodel.UserViewModel
import com.grigorenko.yourchance.databinding.FragmentSignInBinding
import com.grigorenko.yourchance.ui.MainActivity

class SignInFragment : Fragment() {
    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    private val authViewModel: AuthViewModel by activityViewModels()
    private val userViewModel: UserViewModel by activityViewModels()

    private val isGoogleAuthRequired: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        updateUi(authViewModel.checkedForSignedUser())
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authViewModel.firebaseUser.observe(viewLifecycleOwner) { firebaseUser ->
            isGoogleAuthRequired.observe(viewLifecycleOwner) { isGoogleAuth ->
                if (isGoogleAuth && firebaseUser != null) {
                    val user = User(
                        firebaseUser.email.toString(),
                        firebaseUser.displayName.toString(),
                        firebaseUser.phoneNumber.toString(),
                        listOf(),
                        Image(
                            System.currentTimeMillis().toString(),
                            firebaseUser.photoUrl.toString()
                        )
                    )
                    userViewModel.addNewUser(firebaseUser.uid, user)
                }
                updateUi(firebaseUser)
            }
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("802209574608-k7mgkuo2geeh389kkcort2i1iqa5mlj5.apps.googleusercontent.com")
            .requestEmail()
            .build()
        // Build a GoogleSignInClient with the options specified by gso.
        val mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        // Set the dimensions of the sign-in button.

        binding.apply {
            signInButton.setOnClickListener {
                isGoogleAuthRequired.value = false
                if (validateEmail() and validatePassword())
                    authViewModel.authWithEmail(
                        this.emailField.text.toString(),
                        this.passwordField.text.toString()
                    )
            }
            googleSignInButton.setOnClickListener {
                isGoogleAuthRequired.value = true
                val signInIntent = mGoogleSignInClient.signInIntent
                activityResult.launch(signInIntent)
                mGoogleSignInClient.signOut()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        binding.apply {
            emailField.text?.clear()
            emailContainer.error = null
            passwordField.text?.clear()
            passwordContainer.error = null
        }
    }

    private val activityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                // Signed in successfully, show authenticated UI.
                val account = task.getResult(ApiException::class.java)!!
                authViewModel.authWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("ENTER", "signInResult:failed code=" + e.statusCode)
            }
        }

    private fun updateUi(user: FirebaseUser?) {
        if (user != null) {
            val mainActivity = Intent(context, MainActivity::class.java)
            startActivity(mainActivity)
            activity?.finish()
        } else {
            Log.e("AUTH", "User is null")
        }
    }

    private fun validateEmail(): Boolean {
        val emailInput = binding.emailField.text.toString()
        return when {
            emailInput.isEmpty() -> {
                binding.emailContainer.error = "Поле не может быть пустым"
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
            else -> {
                binding.passwordContainer.error = null
                true
            }
        }
    }
}