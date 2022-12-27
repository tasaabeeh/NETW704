package com.example.grocery_signaling

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivityFrag : AppCompatActivity() {
    lateinit var button3: Button
    lateinit var button4: Button
    lateinit var buyButton: Button
    lateinit var sellButton: Button
    lateinit var call: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_frag)

        button3 = findViewById(R.id.button3)
        button4 = findViewById(R.id.button4)
        buyButton = findViewById(R.id.BuyButton)
        sellButton = findViewById(R.id.SellButton)
        call=findViewById(R.id.calling)
        val UserID:String = intent.getStringExtra("User ID").toString()

        button3.setOnClickListener {
            val intent = Intent(this, MainActivityReg::class.java)
            startActivity(intent)
        }
        button4.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }
        buyButton.setOnClickListener {
            val intent = Intent(this, BuyActivity::class.java)
            intent.putExtra("User ID",UserID)
            startActivity(intent)
        }
        sellButton.setOnClickListener {
            val intent = Intent(this, SellActivity::class.java)
            intent.putExtra("User ID",UserID)
            startActivity(intent)
        }
        call.setOnClickListener {
            val intent = Intent(this, Voice::class.java)
            startActivity(intent)
        }




    }


}