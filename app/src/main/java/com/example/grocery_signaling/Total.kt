package com.example.grocery_signaling

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class Total : AppCompatActivity() {
    lateinit var total:String
    lateinit var Amount:TextView
    lateinit var bye: Button
    lateinit var delivery: String
    lateinit var Delivery: TextView
    lateinit var TotalAmount: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_total)

        bye=findViewById(R.id.ByeButton)
        delivery=intent.getStringExtra("Total Delivery Cost").toString()
        Delivery=findViewById(R.id.deliverymoney)
        Delivery.setText(delivery)



        total = intent.getStringExtra("Total Amount").toString()
        Amount=findViewById(R.id.money)
        Amount.setText(total.toString())

        bye.setOnClickListener{
            val intent = Intent(this, BuyActivity::class.java)
            startActivity(intent)
        }
    }


}