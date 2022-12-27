package com.example.grocery_signaling

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

class MyBuyerAdapter(private val buyList: ArrayList<Bought>) :
    RecyclerView.Adapter<MyBuyerAdapter.MyViewHolder>() {
    lateinit var intent: Intent
    lateinit var uri : Uri
    private lateinit var db: DatabaseReference
    lateinit var Prod : String
    var quantity : Int = 0
    private lateinit var db2: DatabaseReference

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val buyView= LayoutInflater.from(parent.context).inflate(R.layout.buy_items,parent,false)
        return MyViewHolder(buyView)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        db = FirebaseDatabase.getInstance().getReference("cart")
        db2= FirebaseDatabase.getInstance().getReference("products")
        val currentProduct:Bought=buyList[position]
        uri = Uri.parse(currentProduct.imageURL)
       // holder.imageProduct.setImageURI(uri)
       // Picasso.get().load(uri).into(holder.imageProduct)
        holder.imageProduct.isVisible
        holder.name.text=currentProduct.name
        holder.price.text=currentProduct.price
        holder.quantity.text=currentProduct.quantity
        holder.serialnumb.text=currentProduct.Serialnum
        holder.addToCarttt.setOnClickListener{
            val product = productB(
                currentProduct.name.toString(),
                1,
                currentProduct.price.toString(),
                currentProduct.Serialnum.toString(),
                currentProduct.UserID
            )
            Prod= db.child("cart").push().key!! //key generated from firebase and !! checks for null
            db.child(Prod).setValue(product).addOnCompleteListener {
                Toast.makeText(holder.context, "Product was added to cart successfully!", Toast.LENGTH_SHORT).show()
            }
            db2.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    quantity= snapshot.child(currentProduct.Serialnum).child("productQuantity").value.toString().toInt()
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

            if(quantity==0){
                Toast.makeText(holder.context, "Product is out of stock!", Toast.LENGTH_SHORT).show()
            }
            else{
                var UpdatedQuantity:Int= quantity - 1
                db2.child(currentProduct.Serialnum).child("productQuantity").setValue(UpdatedQuantity).addOnCompleteListener {
                    Toast.makeText(holder.context, "Product is available!", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    override fun getItemCount(): Int {
        return buyList.size
    }

    class MyViewHolder(buyView: View) : RecyclerView.ViewHolder(buyView){
        val imageProduct : ImageView = buyView.findViewById(R.id.imageProductB)
        val name: TextView = buyView.findViewById(R.id.nameB)
        val price: TextView = buyView.findViewById(R.id.priceB)
        val quantity: TextView = buyView.findViewById(R.id.quantityB)
        val serialnumb: TextView = buyView.findViewById(R.id.serialNumB)
        val addToCarttt : Button = buyView.findViewById(R.id.addingToCart)
        var context: Context =buyView.context
    }
}