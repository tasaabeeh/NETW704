package com.example.grocery_signaling

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import java.lang.Math.toRadians
import kotlin.properties.Delegates

class CartActivity : AppCompatActivity() {

    private lateinit var db: DatabaseReference
    private lateinit var dbnew: DatabaseReference
    private lateinit var dbLoc: DatabaseReference
    lateinit var UserID: String
    lateinit var NewNameBuyer: String
    lateinit var NewQuantityBuyer: String
    lateinit var NewPriceBuyer: String
    lateinit var NewSerialNumberBuyer: String
    lateinit var CartSerialNumB: String
    lateinit var NewArrayListBuyer: ArrayList<Cart>
    private lateinit var NewRecyclerViewB: RecyclerView
    var TotalCost:Int = 0
    lateinit var orderButton: Button
    lateinit var intent1:Intent
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationUser:LatLng
    lateinit var seller : String
    lateinit var locationSeller: LatLng
    private lateinit var LocationnArrayList: ArrayList<LatLng>
    var DeliveryCost:Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        UserID = intent.getStringExtra("User ID").toString()
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this)
        LocationnArrayList= arrayListOf<LatLng>()
        intent1 = Intent(this@CartActivity, Total::class.java)

        val task: Task<Location> = fusedLocationProviderClient.lastLocation
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 101)
            return
        }
        task.addOnSuccessListener {
            if(it!=null){
                locationUser=LatLng(it.latitude,it.longitude)
            }
        }




        NewRecyclerViewB = findViewById(R.id.RecyclerViewCart)
        NewRecyclerViewB.layoutManager = LinearLayoutManager(this)
        NewRecyclerViewB.setHasFixedSize(false)
        NewArrayListBuyer = arrayListOf<Cart>()
        getCartData()


        orderButton=findViewById(R.id.OrderNow)
        orderButton.setOnClickListener{
            db = FirebaseDatabase.getInstance().getReference("cart")
            dbnew= FirebaseDatabase.getInstance().getReference("products")
            dbLoc=FirebaseDatabase.getInstance().getReference("locations")
            db.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    println(LocationnArrayList)
                    for (childSnapshot in snapshot.children) {
                        if (childSnapshot.child("buyer").value.toString().equals(UserID)){
                            NewQuantityBuyer=childSnapshot.child("productQuantity").value.toString() //ADD QUANTITY TO QUANT LIST
                            NewPriceBuyer=childSnapshot.child("productPrice").value.toString() // ADD PRICE TO PRICE LIST
                            NewSerialNumberBuyer=childSnapshot.child("serialnumberr").value.toString() //ADD SERIAL NUM TO SERIAL LIST
                            val Cartproduct= Cart(NewNameBuyer,1,NewPriceBuyer,NewSerialNumberBuyer,CartSerialNumB)
                            NewArrayListBuyer.add(Cartproduct)
                            val x=Integer.parseInt(NewPriceBuyer)
                            val y=Integer.parseInt(NewQuantityBuyer)
                            val z= x*y
                            TotalCost=TotalCost + z
                            dbnew.addValueEventListener(object : ValueEventListener{
                                override fun onDataChange(snapshot: DataSnapshot) {
                                   seller= snapshot.child(NewSerialNumberBuyer).child("seller").value.toString()
                                    dbLoc.addValueEventListener(object : ValueEventListener{
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            for (childSnapshot in snapshot.children){
                                                if(childSnapshot.child("UserID").value.toString().equals(seller)){
                                                    locationSeller= LatLng(childSnapshot.child("latitude").value.toString().toDouble(),
                                                        childSnapshot.child("longitude").value.toString().toDouble())
                                                    if(!LocationnArrayList.contains(locationSeller)){
                                                        LocationnArrayList!!.add(locationSeller)
                                                        //println("AAAAAAAAAAAAAAAAAAAAAAAAHHHHHHHHHHHHHHHHHHHHHHHHHHH")
                                                    }
                                                    println(LocationnArrayList)
                                                    //println("AAAAAAAAAAAAAAAAAAAAAAAAHHHHHHHHHHHHHHHHHHHHHHHHHHH11111")

                                                }
                                            }
                                            for(i in LocationnArrayList.indices) {
                                                DeliveryCost= DeliveryCost+ getDeliveryCost(locationUser, LocationnArrayList[i])
                                            }
                                            //intent1.putExtra("Total Amount", TotalCost.toString())
                                            //println(DeliveryCost)
                                            intent1.putExtra("Total Delivery Cost", DeliveryCost.toString())
                                            startActivity(intent1)
                                            //println(DeliveryCost)
                                           // println(LocationnArrayList)
                                        }

                                        override fun onCancelled(error: DatabaseError) {
                                            TODO("Not yet implemented")
                                        }



                                    })

                                }

                                override fun onCancelled(error: DatabaseError) {
                                    TODO("Not yet implemented")
                                }

                            })
                        }
                    }
                    //fetchLocation()
                    //for(i in LocationnArrayList.indices) {
                     // DeliveryCost= DeliveryCost+ getDeliveryCost(locationUser, LocationnArrayList[i])
                   // }
                   // intent1 = Intent(this@CartActivity, Total::class.java)
                    intent1.putExtra("Total Amount", TotalCost.toString())
                    //intent1.putExtra("Total Delivery Cost", DeliveryCost.toString())
                    startActivity(intent1)
                }


                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

        }

    }

    private fun fetchLocation(){
        val task: Task<Location> = fusedLocationProviderClient.lastLocation
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
        != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 101)
            return
        }
        task.addOnSuccessListener {
            if(it!=null){
                locationUser=LatLng(it.latitude,it.longitude)
            }
        }
    }

    private fun getDeliveryCost(locationUser:LatLng,locationMarket:LatLng):Double{
        val earthRadius=6371.0
        val lat1=toRadians(locationUser.latitude)
        val lon1= toRadians(locationUser.longitude)
        val lat2 = toRadians(locationMarket.latitude)
        val lon2= toRadians(locationMarket.longitude)
        val latDistance= lat2-lat1
        val lonDistance= lon2-lon1
        val a = Math.sin(latDistance/2)* Math.sin(latDistance/2)+
                Math.cos(lat1)* Math.cos(lat2)*
                Math.sin(lonDistance/2)*Math.sin(lonDistance/2)
        val c = 2*Math.atan2(Math.sqrt(a),Math.sqrt(1-a))
        val distance= earthRadius* c
        return distance*10

    }

    private fun getCartData() {
        db = FirebaseDatabase.getInstance().getReference("cart")
        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (childSnapshot in snapshot.children) {
                    //println(childSnapshot.child("buyer").value)
                   // println(UserID)
                    if (childSnapshot.child("buyer").value.toString().equals(UserID)){
                        NewNameBuyer = childSnapshot.child("productName").value.toString() //ADD PRODUCT NAME TO NAME LIST
                        NewQuantityBuyer=childSnapshot.child("productQuantity").value.toString() //ADD QUANTITY TO QUANT LIST
                        NewPriceBuyer=childSnapshot.child("productPrice").value.toString() // ADD PRICE TO PRICE LIST
                        NewSerialNumberBuyer=childSnapshot.child("serialnumberr").value.toString() //ADD SERIAL NUM TO SERIAL LIST
                        CartSerialNumB=childSnapshot.key.toString()
                        val Cartproduct= Cart(NewNameBuyer,1,NewPriceBuyer,NewSerialNumberBuyer,CartSerialNumB)
                        NewArrayListBuyer.add(Cartproduct)
                }
                }
                NewRecyclerViewB.adapter=MyCartAdapter(NewArrayListBuyer)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun getCost(){
        db = FirebaseDatabase.getInstance().getReference("cart")
        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (childSnapshot in snapshot.children) {
                    if (childSnapshot.child("buyer").value.toString().equals(UserID)){
                        NewQuantityBuyer=childSnapshot.child("productQuantity").value.toString() //ADD QUANTITY TO QUANT LIST
                        NewPriceBuyer=childSnapshot.child("productPrice").value.toString() // ADD PRICE TO PRICE LIST
                        NewSerialNumberBuyer=childSnapshot.child("serialnumberr").value.toString() //ADD SERIAL NUM TO SERIAL LIST
                        val Cartproduct= Cart(NewNameBuyer,1,NewPriceBuyer,NewSerialNumberBuyer,CartSerialNumB)
                        NewArrayListBuyer.add(Cartproduct)
                        val x=Integer.parseInt(NewPriceBuyer)
                        val y=Integer.parseInt(NewQuantityBuyer)
                        val z= x*y
                        //println(z)
                        TotalCost=TotalCost + z
                }
            }
            }


            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        //return TotalCost
    }

}