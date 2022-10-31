package com.example.grocery_signaling

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivityFrag : AppCompatActivity() {
    lateinit var button3: Button
    lateinit var button4: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_frag)

        button3 = findViewById(R.id.button3)
        button4 = findViewById(R.id.button4)

        button3.setOnClickListener {
            val intent = Intent(this, MainActivityReg::class.java)
            startActivity(intent)
        }
        button4.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }
    }


}