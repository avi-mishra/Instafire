package android.example.instafire

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {

    private val firebaseUser: FirebaseUser? =FirebaseAuth.getInstance().currentUser
    private val signInLauncher = registerForActivityResult(
            FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(firebaseUser!=null)
        {
            postActivity()
        }
        else{
            val providers = arrayListOf(
                    AuthUI.IdpConfig.PhoneBuilder().build(),
                    AuthUI.IdpConfig.GoogleBuilder().build())

            val signInIntent = AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setIsSmartLockEnabled(false)
                    .setAvailableProviders(providers)
                    .build()
            signInLauncher.launch(signInIntent)
        }
    }
    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == RESULT_OK) {
            // Successfully signed in
            postActivity()
        } else {
            if (response == null) {
                // User pressed back button
                Toast.makeText(this@MainActivity,"Back button pressed!",Toast.LENGTH_SHORT).show()
                return;
            }

            if (response.error?.errorCode == ErrorCodes.NO_NETWORK) {
                return;
            }
            Toast.makeText(this@MainActivity,"Sign In error!",Toast.LENGTH_SHORT).show()
        }
    }
    private fun postActivity() {
        val i=Intent(this@MainActivity,PostsActivity::class.java)
        startActivity(i)
        finish()
    }
}