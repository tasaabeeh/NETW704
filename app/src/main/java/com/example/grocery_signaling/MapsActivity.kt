package com.example.grocery_signaling

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.grocery_signaling.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val Location_Request = 1
    private lateinit var db: DatabaseReference
    private lateinit var LocationArrayList: ArrayList<LatLng>
    var longitude:Double = 0.0
    var latitude:Double = 0.0
    private lateinit var UserIDArrayList: ArrayList<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      //  binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_maps)
        getMarkers()

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    private fun getMarkers() {
        db = FirebaseDatabase.getInstance().getReference("locations")
        LocationArrayList= arrayListOf<LatLng>()
        UserIDArrayList= arrayListOf<String>()
        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (childSnapshot in snapshot.children) {
                    longitude =childSnapshot.child("longitude").value.toString().toDouble()
                    latitude=childSnapshot.child("latitude").value.toString().toDouble()
                    val loc=LatLng(longitude,latitude)
                    LocationArrayList!!.add(loc)
                    var userName:String=childSnapshot.child("UserID").value.toString()
                    UserIDArrayList!!.add(userName)
                }
                //println(LocationArrayList)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        Get_Location_Access()
        for(i in LocationArrayList!!.indices){
            mMap.addMarker(MarkerOptions().position(LocationArrayList!![i]).title(UserIDArrayList[i]))
                //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)))
            mMap.animateCamera(CameraUpdateFactory.zoomTo(18.0f))
            mMap.moveCamera(CameraUpdateFactory.newLatLng(LocationArrayList!![i]))
        }
    }


    private fun Get_Location_Access() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                Location_Request
            )

    }
     fun OnRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == Location_Request) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            mMap.isMyLocationEnabled = true
            }
            else {
                Toast.makeText(this, "User has not granted location access Permission", Toast.LENGTH_LONG).show()
                finish()
            }
        }

   // fun getDistanceBetweenMarkers(marker1:LatLng,marker2:LatLng):Double{
   //     val earthRadius
   // }

    }

