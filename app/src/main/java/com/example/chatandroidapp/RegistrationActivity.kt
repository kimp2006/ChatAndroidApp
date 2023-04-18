package com.example.chatandroidapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.chatandroidapp.databinding.ActivityRegistrationBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase

class RegistrationActivity : AppCompatActivity() {

    private var binding: ActivityRegistrationBinding? = null
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        auth = Firebase.auth

        binding?.button?.setOnClickListener {

            val email = binding?.editTextTextEmailAddress?.text.toString()
            val password = binding?.editTextTextPassword?.text.toString()
            val name = binding?.editTextTextPersonName?.text.toString()

            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{
                if (it.isSuccessful) {
                  updateProfile(name)
                } else {
                    Toast.makeText(applicationContext, it.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }

        }

    }

    private fun updateProfile(name: String){
        auth.currentUser!!.updateProfile(userProfileChangeRequest {
            displayName = name
            photoUri = Uri.EMPTY
        }).addOnCompleteListener{result ->
            if (result.isSuccessful){
                startActivity(Intent(applicationContext, LoginActivity::class.java))
            }
        }
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}