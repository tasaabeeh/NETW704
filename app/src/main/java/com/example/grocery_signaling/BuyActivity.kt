package com.example.grocery_signaling

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage

class BuyActivity : AppCompatActivity() {
    private lateinit var newrecyclerview : RecyclerView
    private lateinit var newarraylist : ArrayList<Bought>
    lateinit var addToCart:Button
    lateinit var NewName: String
    lateinit var NewQuantity: String
    lateinit var NewPrice: String
    lateinit var NewSerialNumber: String
    lateinit var imageURL: String
    lateinit var view1: View
    private lateinit var db: DatabaseReference
    lateinit var UserID: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buy)
        UserID = intent.getStringExtra("User ID").toString()
        val checkout: ExtendedFloatingActionButton = findViewById(R.id.addToCart)
        var inflaterr: LayoutInflater =layoutInflater
        view1=inflaterr.inflate(R.layout.buy_items,null)

        addToCart =view1.findViewById(R.id.addingToCart)
        db = FirebaseDatabase.getInstance().getReference("cart")
        newrecyclerview = findViewById(R.id.recyclerView1)
        newrecyclerview.layoutManager = LinearLayoutManager(this)
        newrecyclerview.setHasFixedSize(false)
        newarraylist = arrayListOf<Bought>()
        getBuyerData()

        checkout.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            intent.putExtra("User ID",UserID)
            startActivity(intent)
            }
        }

    private fun getBuyerData() {
        db = FirebaseDatabase.getInstance().getReference("products")
        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (childSnapshot in snapshot.children) {
                    NewName = childSnapshot.child("productName").value.toString() //ADD PRODUCT NAME TO NAME LIST
                    NewQuantity=childSnapshot.child("productQuantity").value.toString() //ADD QUANTITY TO QUANT LIST
                    NewPrice=childSnapshot.child("productPrice").value.toString() // ADD PRICE TO PRICE LIST
                    NewSerialNumber=childSnapshot.key.toString() //ADD SERIAL NUM TO SERIAL LIST
                    val imageID = childSnapshot.key.toString()
                    val storageReference = FirebaseStorage.getInstance()
                    var ref = storageReference.getReferenceFromUrl("gs://grocery-signaling.appspot.com")
                    var pathRef = ref.child("$imageID.jpeg")
                    imageURL= childSnapshot.child("imageUri").value.toString()
                    val buyproduct= Bought(imageURL,NewName,NewPrice,NewQuantity,NewSerialNumber,UserID)
                    newarraylist.add(buyproduct)
                }
                newrecyclerview.adapter=MyBuyerAdapter(newarraylist)
                }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
                }
            })
        }
    }


