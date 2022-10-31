package com.example.grocery_signaling

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivityReg : AppCompatActivity() {
    lateinit var button6: Button
    lateinit var editText6: EditText
    lateinit var editText7: EditText
    lateinit var editText8: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_reg)

        button6 = findViewById(R.id.button6)
        editText6=findViewById(R.id.editTextTextPersonName2)
        editText7=findViewById(R.id.editTextTextPersonName4)
        editText8=findViewById(R.id.editTextTextPersonName5)

        val Email: String? = intent.getStringExtra("email")
        val Password: String? = intent.getStringExtra("password")
        val data = User(Email.toString(), Password.toString())

        data.setFirstName(editText6.getText().toString())
        data.setLastName(editText7.getText().toString())
        data.setPhoneNumber(editText8.getText().toString())

        button6.setOnClickListener(){
            Toast.makeText(this,"Updated successfully!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivityFrag::class.java)
            intent.putExtra("User Data",data.toString())
            startActivity(intent)
        }

    }


}