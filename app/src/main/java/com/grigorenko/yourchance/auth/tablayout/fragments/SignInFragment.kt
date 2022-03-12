package com.grigorenko.yourchance.auth.tablayout.fragments

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.grigorenko.yourchance.MainActivity
import com.grigorenko.yourchance.database.repo.FirebaseAuthRepo.Companion.firebaseAuth
import com.grigorenko.yourchance.databinding.FragmentSignInBinding

class SignInFragment(private val signedUser: FirebaseUser?) : Fragment() {
    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateUi(signedUser)

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
                if (validateEmail() and validatePassword())
                    authWithEmail(
                        this.emailField.text.toString(),
                        this.passwordField.text.toString()
                    )
            }
            googleSignInButton.setOnClickListener {
                val signInIntent = mGoogleSignInClient.signInIntent
                activityResult.launch(signInIntent)
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
                authWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("ENTER", "signInResult:failed code=" + e.statusCode)
            }
        }

    private fun authWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    updateUi(user)
                } else {
                    updateUi(null)
                }
            }
    }

    private fun authWithEmail(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = firebaseAuth.currentUser
                    updateUi(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        context, "Неверные данные авторизации!!",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUi(null)
                }
            }
    }

    private fun updateUi(user: FirebaseUser?) {
        if (user != null) {
            val mainActivity = Intent(context, MainActivity::class.java)
            user.apply {
                mainActivity.putExtra("Email", this.email)
                mainActivity.putExtra("Name", this.displayName)
                mainActivity.putExtra("Phone", this.phoneNumber)
                mainActivity.putExtra("Photo", this.photoUrl)
            }
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