package com.example.grocery_signaling

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LongLat : AppCompatActivity() {
    lateinit var savedata: Button
    lateinit var longitude: EditText
    lateinit var latitude: EditText
    lateinit var UserID: String
    private lateinit var db: DatabaseReference
    lateinit var LocationSerialNum: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_long_lat)
        savedata=findViewById(R.id.savedata)
        longitude=findViewById(R.id.longitude)
        latitude=findViewById(R.id.latitude)
        UserID = intent.getStringExtra("User ID").toString()

        savedata.setOnClickListener {
            db = FirebaseDatabase.getInstance().getReference("locations")
            LocationSerialNum = db.child("locations")
                .push().key!!
            db.child(LocationSerialNum).child("longitude").setValue(longitude.text.toString()).addOnCompleteListener {
                Toast.makeText(this, "Longitude was added successfully!", Toast.LENGTH_SHORT).show()
            }
            db.child(LocationSerialNum).child("latitude").setValue(latitude.text.toString()).addOnCompleteListener {
                Toast.makeText(this, "Latitude was added successfully!", Toast.LENGTH_SHORT).show()
            }
            db.child(LocationSerialNum).child("UserID").setValue(UserID).addOnCompleteListener {
                Toast.makeText(this, "UserID was added successfully!", Toast.LENGTH_SHORT).show()
            }
        }



    }
}