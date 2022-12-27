package com.example.grocery_signaling

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.grocery_signaling.databinding.ActivityUploadProductBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage


class UploadProduct : AppCompatActivity() {
    lateinit var uploadpic: Button
    lateinit var saveButton: Button
    lateinit var image: ImageView
    private lateinit var db: DatabaseReference
    lateinit var binding: ActivityUploadProductBinding
    lateinit var ProductQuantity: EditText
    lateinit var ProductName: EditText
    lateinit var ProductPrice: EditText
    lateinit var ProductSerialNum: String
    lateinit  var ImageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_product)

        saveButton = findViewById(R.id.SaveButton)
        uploadpic = findViewById(R.id.Uploadpic)
        image = findViewById(R.id.image)
        val UserID:String = intent.getStringExtra("User ID").toString()
        db = FirebaseDatabase.getInstance().getReference("products")

        saveButton.setOnClickListener {
            ProductName = findViewById(R.id.productName)
            ProductPrice = findViewById(R.id.productPrice)
            ProductQuantity = findViewById(R.id.productQuantity)
            image = findViewById(R.id.image)
            ProductSerialNum = db.child("products")
                .push().key!! //key generated from firebase and !! checks for null
            val fileName = ProductSerialNum.toString()
            val storageRef = FirebaseStorage.getInstance().getReference(fileName)
            storageRef.putFile(ImageUri) //PUT IMAGE IN STORAGE

            val product = Product(
                ProductName.text.toString(),
                ProductQuantity.text.toString(),
                ProductPrice.text.toString(),
                UserID,
                ImageUri.toString()
            )
            db.child(ProductSerialNum).setValue(product).addOnCompleteListener {
                Toast.makeText(this, "Product was added successfully!", Toast.LENGTH_SHORT).show()
            }
            val intent = Intent(this, SellActivity::class.java)
            startActivity(intent)
        }
                val getAction = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
                    ImageUri= it?.data?.getData()!!
                    image.setImageURI(ImageUri)
                   // println(ImageUri)
            }



            uploadpic.setOnClickListener {
                val intent =Intent(Intent.ACTION_PICK,MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                getAction.launch(intent)
            }


        }



}