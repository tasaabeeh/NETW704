package com.example.grocery_signaling

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {
    lateinit var button5: Button
    lateinit var editTextText5: EditText
    lateinit var editTextText6: EditText
    lateinit var editTextText7: EditText
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        editTextText5 = findViewById(R.id.editTextTextEmailAddress2)
        editTextText6 = findViewById(R.id.editTextTextPassword)
        editTextText7 = findViewById(R.id.editTextTextPassword3)
        button5 = findViewById(R.id.button5)

        button5.setOnClickListener {
            firebaseAuth = FirebaseAuth.getInstance()
            val email = editTextText5.getText().toString().trim()
            val password = editTextText6.getText().toString().trim()
            val conf_pass=editTextText7.getText().toString().trim()
            if (email.isNotEmpty() && password.isNotEmpty() && conf_pass.isNotEmpty()) {
                if(password==conf_pass){
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            Toast.makeText(this, "${email} is Registered!", Toast.LENGTH_SHORT).show()
                        }
                        else{
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                else {
                    Toast.makeText(this, "Passwords do not match!.", Toast.LENGTH_SHORT).show()
                        }}

            else{
                Toast.makeText(this,"Please fill the required fields!", Toast.LENGTH_SHORT).show()
                }
    }
    }
}