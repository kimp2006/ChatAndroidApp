package com.example.chatandroidapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.chatandroidapp.databinding.ActivityMainBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.Date

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null
    private val auth = Firebase.auth
    private lateinit var database: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        database = Firebase.database.getReference("messages")
        val name = auth.currentUser?.displayName.toString()

        binding?.nameView?.text = name

        binding?.logout?.setOnClickListener {
            auth.signOut()
            startActivity(Intent(applicationContext, LoginActivity::class.java))
        }


        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val stringBuilder = StringBuilder()
                for (snap in snapshot.children) {
                    val message = snap.getValue(Message::class.java)
                    stringBuilder
                        .append("${message?.date}: (${message?.sender})  ${message?.message} ")
                        .append('\n')
                }

                binding?.outputTextView?.text = stringBuilder
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })


        binding?.sendBtn?.setOnClickListener {
            val message = binding?.messageEditText?.text.toString()
            binding?.messageEditText?.setText("")
            binding?.outputTextView?.text = ""
            database.push().setValue(Message(name, Date().toString(), message))


        }
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}