package com.example.grocery_signaling

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class MyCartAdapter( private val allProducts : ArrayList<Cart>) : RecyclerView.Adapter<MyCartAdapter.MyViewHolder>() {

    private lateinit var db: DatabaseReference
    private lateinit var db2: DatabaseReference
    var quantity:Int=0


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyCartAdapter.MyViewHolder {
        val productView = LayoutInflater.from(parent.context).inflate(R.layout.cart_items,parent,false)
        return MyViewHolder(productView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentProduct: Cart=allProducts[position]
        holder.name.text=currentProduct.namecart
        holder.price.text=currentProduct.pricecart
        holder.quantity.text= currentProduct.quantitycart.toString()
        holder.Serialnum.text=currentProduct.serialnumcart
        db2= FirebaseDatabase.getInstance().getReference("products")

        holder.decrementButton.setOnClickListener{
            if(currentProduct.quantitycart.equals(1)){
                db=FirebaseDatabase.getInstance().reference.child("cart").child(currentProduct.CartSerialNumber)
                db.removeValue()
                holder.quantity.text= currentProduct.quantitycart.toString()
                Toast.makeText(holder.context, "Product was added to cart successfully!", Toast.LENGTH_SHORT).show()
            }
            else{
                currentProduct.quantitycart=currentProduct.quantitycart-1
                holder.quantity.text= currentProduct.quantitycart.toString()
                db2.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        quantity= snapshot.child(currentProduct.serialnumcart).child("productQuantity").value.toString().toInt()
                    }
                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
                var UpdatedQuantity:Int= quantity + 1
                db2.child(currentProduct.serialnumcart).child("productQuantity").setValue(UpdatedQuantity).addOnCompleteListener {
                    Toast.makeText(holder.context, "Product is returned!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        holder.incrementButton.setOnClickListener{

            db2.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    quantity= snapshot.child(currentProduct.serialnumcart).child("productQuantity").value.toString().toInt()
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
                db2.child(currentProduct.serialnumcart).child("productQuantity").setValue(UpdatedQuantity).addOnCompleteListener {
                    Toast.makeText(holder.context, "Product is available!", Toast.LENGTH_SHORT).show()
                }
                currentProduct.quantitycart=currentProduct.quantitycart+1
                holder.quantity.text= currentProduct.quantitycart.toString()
            }
        }
        //println(currentProduct.quantitycart.toString())
    }


    override fun getItemCount(): Int {
        return allProducts.size
    }

    class MyViewHolder(productView : View) : RecyclerView.ViewHolder(productView) {
        val name: TextView = productView.findViewById(R.id.nameCart)
        val price: TextView = productView.findViewById(R.id.priceCart)
        val quantity: TextView = productView.findViewById(R.id.quantityCart)
        val Serialnum: TextView = productView.findViewById(R.id.serialNumCart)
        val incrementButton : Button = productView.findViewById(R.id.increment)
        val decrementButton : Button = productView.findViewById(R.id.decrement)
        var context: Context =productView.context
    }

}