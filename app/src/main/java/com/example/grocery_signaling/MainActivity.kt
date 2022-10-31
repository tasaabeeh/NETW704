package com.example.grocery_signaling

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    lateinit var button: Button
    lateinit var button2: Button
    lateinit var editText1: EditText
    lateinit var editText2: EditText
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        editText1 = findViewById(R.id.editTextTextEmailAddress)
        editText2 = findViewById(R.id.editTextTextPassword2)
        button = findViewById(R.id.button)
        button2 = findViewById(R.id.button2)
        firebaseAuth = FirebaseAuth.getInstance()

        button.setOnClickListener {
            val email = this.editText1.getText().toString().trim()
            val password = this.editText2.getText().toString().trim()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) {
                    if (it.isSuccessful) {
                        val intent = Intent(this, MainActivityFrag::class.java)
                        startActivity(intent)
                        //Toast.makeText(this,it.exception.toString(),Toast.LENGTH_SHORT).show()
                        Toast.makeText(this,"Logged in successfully!",Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Email or Password is incorrect! Please try again.", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Please fill the required fields!", Toast.LENGTH_SHORT).show()
            }
        }

        button2.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)

        }


    }
}



