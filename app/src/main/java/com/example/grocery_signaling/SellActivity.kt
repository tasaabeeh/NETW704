package com.example.grocery_signaling

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.grocery_signaling.databinding.ActivitySellBinding
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import java.io.File
import android.view.View
import androidx.core.view.isVisible
import com.example.grocery_signaling.databinding.ActivityBuyBinding.inflate
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.slider.Slider.OnChangeListener
import com.google.firebase.storage.UploadTask


class SellActivity : AppCompatActivity() {
    lateinit var addNewProduct: Button
    private lateinit var NewRecyclerView: RecyclerView
    private lateinit var NewArrayList: ArrayList<Sold>
    lateinit var EditButton:Button
    lateinit var image: ImageView
    lateinit var UserID: String
    lateinit var NewName: String
    lateinit var NewQuantity: String
    lateinit var NewPrice: String
    lateinit var NewSerialNumber: String
    lateinit var imageURL: String
    lateinit var imageProd: String
    lateinit var view: View
    lateinit var locationButton: Button


    private lateinit var db: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sell)
        val addNewProduct: ExtendedFloatingActionButton = findViewById(R.id.addNewProduct)
        UserID = intent.getStringExtra("User ID").toString()
        var inflater:LayoutInflater=layoutInflater
        view=inflater.inflate(R.layout.sell_items,null)
        EditButton=view.findViewById<Button>(R.id.edit)
        locationButton=findViewById(R.id.location)

        NewRecyclerView = findViewById(R.id.recyclerView)
        NewRecyclerView.layoutManager = LinearLayoutManager(this)
        NewRecyclerView.setHasFixedSize(false)
        NewArrayList = arrayListOf<Sold>()
        getSellerData()

        EditButton.setOnClickListener{
            val intent = Intent(this, EditActivity::class.java)
            startActivity(intent)
        }

        addNewProduct.setOnClickListener {
            val intent = Intent(this, UploadProduct::class.java)
            intent.putExtra("User ID", UserID)
            startActivity(intent)
        }

        locationButton.setOnClickListener {
            val intent = Intent(this, LongLat::class.java)
            intent.putExtra("User ID",UserID)
            startActivity(intent)
        }
    }

    private fun getSellerData() {
        db = FirebaseDatabase.getInstance().getReference("products")
        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (childSnapshot in snapshot.children) {
                    if (childSnapshot.child("seller").value.toString().equals(UserID)){
                        NewName = childSnapshot.child("productName").value.toString() //ADD PRODUCT NAME TO NAME LIST
                        NewQuantity=childSnapshot.child("productQuantity").value.toString() //ADD QUANTITY TO QUANT LIST
                        NewPrice=childSnapshot.child("productPrice").value.toString() // ADD PRICE TO PRICE LIST
                        NewSerialNumber=childSnapshot.key.toString() //ADD SERIAL NUM TO SERIAL LIST
                        val imageID = childSnapshot.key.toString()
                        val storageReference = FirebaseStorage.getInstance()
                        var ref = storageReference.getReferenceFromUrl("gs://grocery-signaling.appspot.com")
                        var pathRef = ref.child("$imageID.jpeg")
                        imageURL= childSnapshot.child("imageUri").value.toString()
                        val sellproduct= Sold(imageURL,NewName,NewPrice,NewQuantity,NewSerialNumber)//,image)
                        NewArrayList.add(sellproduct)
                }
            }
                //println(NewArrayList)
                NewRecyclerView.adapter=MyAdapterSeller(NewArrayList)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}
            //db = FirebaseDatabase.getInstance().getReference("products")
            //db.addValueEventListener(object : ValueEventListener {
                //override fun onDataChange(dataSnapshot: DataSnapshot) {
                    //for (childSnapshot in dataSnapshot.children) { //LOOP OVER ALL PRODUCTS
                        //if (childSnapshot.child("seller").value.toString()
                          //      .equals(UserID) ) { //CHECK IF SELLER IS CURRENT USER
                           // NewName.add(childSnapshot.child("productName").value.toString()) //ADD PRODUCT NAME TO NAME LIST
                            //NewQuantity.add(childSnapshot.child("productQuantity").value.toString()) //ADD QUANTITY TO QUANT LIST
                           // NewPrice.add(childSnapshot.child("productPrice").value.toString()) // ADD PRICE TO PRICE LIST
                           // NewSerialNumber.add(childSnapshot.key.toString()) //ADD SERIAL NUM TO SERIAL LIST
                           // val imageID = childSnapshot.key.toString() //SERIAL NUMBER SAVED IN VARIABLE
                            //val storageReference = FirebaseStorage.getInstance().reference.child("$imageID.jpeg")//GET IMAGE FROM STORAGE
                            //val localFile = File.createTempFile("$imageID",".jpeg") //IMAGE IN TEMP
                            //val storageReference = FirebaseStorage.getInstance()
                            //var ref = storageReference.getReferenceFromUrl("gs://grocery-signaling.appspot.com")
                            //var pathRef = ref.child("$imageID.jpeg")
                            //imageURL.add(pathRef.toString())
                            //image?.let { .imageProd.add(it) }
                        //}
                    //}
                    //saveArray(NewName, NewQuantity, NewPrice, NewSerialNumber, imageProd,imageURL)

                //}

              //  override fun onCancelled(error: DatabaseError) {
               //     TODO("Not yet implemented")
               // }

           // })
            /* addNewProduct.setOnClickListener {
                val intent = Intent(this, UploadProduct::class.java)
                intent.putExtra("User ID", UserID)
                startActivity(intent)
            }*/
            /*NewRecyclerView = findViewById(R.id.recyclerView)
        NewRecyclerView.layoutManager=LinearLayoutManager(this)
       // val linearLayoutManager : LinearLayoutManager= LinearLayoutManager(this)
        //linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        //NewRecyclerView.setLayoutManager(linearLayoutManager)
        NewRecyclerView.setHasFixedSize(false)
        NewArrayList= arrayListOf<Sold>()
        getSellerData()*/

            /*addNewProduct.setOnClickListener {
            val intent = Intent(this, UploadProduct::class.java)
            intent.putExtra("User ID",UserID)
            startActivity(intent)
        }*/

            //  println(NewName)

        //}
        /*fun getSellerData() { //HATEITHA GOWA EL ON DATA CHANGE
        for (i in 0.. imageURL.size-1){
            val prods=Sold(imageURL[i], NewName[i],NewPrice[i],NewQuantity[i],NewSerialNumber[i],imageProd[i])
            NewArrayList.add(prods)
        }
        NewRecyclerView.adapter=MyAdapterSeller(NewArrayList)
       // var adap= NewRecyclerView.adapter
        //adap=MyAdapterSeller(NewArrayList)
        //NewRecyclerView.setAdapter(adap)
    }

    fun saveArray(NewName: ArrayList<String>, NewQuantity: ArrayList<String>,
                  NewPrice: ArrayList<String>, NewSerialNumber: ArrayList<String>,
                  imageProd: ArrayList<ImageView>,imageURL: ArrayList<String>){
        //NewName1= NewName
        NewPrice1=NewPrice
        NewQuantity1=NewQuantity
        imageProd1=imageProd
        imageURL1=imageURL
        NewSerialNumber1=NewSerialNumber
        var NewName1=Log.i("after","valu" + NewName.toString())
      //  println(Log.println(Log.ASSERT,"valu" + NewName.toString(),"signalling"))
        println(NewName1.digitToChar())
    }*/


    //}
