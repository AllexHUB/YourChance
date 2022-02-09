package com.grigorenko.yourchance

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class GoogleAuthActivity : AppCompatActivity() {

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_auth)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("802209574608-k7mgkuo2geeh389kkcort2i1iqa5mlj5.apps.googleusercontent.com")
            .requestEmail()
            .build()

        // Build a GoogleSignInClient with the options specified by gso.
        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        val signedUser = GoogleSignIn.getLastSignedInAccount(this)
        if (signedUser != null)
            updateUi(true)

        // Set the dimensions of the sign-in button.
        val signInButton = findViewById<SignInButton>(R.id.sign_in_button)
        signInButton.setOnClickListener {
            val signInIntent = mGoogleSignInClient.signInIntent
            activityResult.launch(signInIntent)
        }
    }

    private val activityResult =
        registerForActivityResult(StartActivityForResult()) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                // Signed in successfully, show authenticated UI.
                Log.d("ENTER", "signInResult:entered!")
                val account = task.getResult(ApiException::class.java)!!
                authWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // The ApiException status code indicates the detailed failure reason.
                // Please refer to the GoogleSignInStatusCodes class reference for more information.
                Log.w("ENTER", "signInResult:failed code=" + e.statusCode)
            }
        }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            // Signed in successfully, show authenticated UI.
            Log.d("ENTER", "signInResult:entered!")
            val account = completedTask.getResult(ApiException::class.java)!!
            authWithGoogle(account.idToken!!)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("ENTER", "signInResult:failed code=" + e.statusCode)
        }
    }

    private fun authWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    updateUi(true)
                } else {
                    updateUi(false)
                }
            }
    }

    private fun updateUi(needUpdate: Boolean) {
        if (needUpdate) {
            Log.d("AUTH", "User exists")
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            Log.e("AUTH", "User is null")
        }
    }
}