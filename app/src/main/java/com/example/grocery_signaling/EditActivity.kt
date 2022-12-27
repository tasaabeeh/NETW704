package com.example.grocery_signaling

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class EditActivity : AppCompatActivity() {

    lateinit var Name: EditText
    lateinit var Price: EditText
    lateinit var Quantity: EditText
    lateinit var db: DatabaseReference
    lateinit var Save: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        //var Name1:EditText = intent.getStringExtra("Name")
        //var Price1:EditText = intent.getStringExtra("Price")
        //var Quantity1:EditText = intent.getStringExtra("Quantity")
        var SerialNumber:String = intent.getStringExtra("Serial Number").toString()

        Name= findViewById(R.id.Name)
        Price=findViewById(R.id.Price)
        Quantity=findViewById(R.id.Quantity)
        Save=findViewById(R.id.saveB)



        db = FirebaseDatabase.getInstance().getReference("products")

        Save.setOnClickListener{
            var Name1:String =Name.text.toString()
            var Price1:String =Price.text.toString()
            var Quantity1:String =Quantity.text.toString()
            db.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (childSnapshot in snapshot.children) {
                        if (childSnapshot.key.equals(SerialNumber)){
                            childSnapshot.ref.child("productName").setValue(Name1)
                            childSnapshot.ref.child("productPrice").setValue(Price1)
                            childSnapshot.ref.child("productQuantity").setValue(Quantity1)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }

    }
}