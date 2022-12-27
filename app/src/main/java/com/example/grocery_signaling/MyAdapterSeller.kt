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
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso


class MyAdapterSeller( private val products : ArrayList<Sold>) : RecyclerView.Adapter<MyAdapterSeller.MyViewHolder>(){ //BEYHOT FEL SOLDVIEWW
  //  lateinit var imageProduct : ImageView
    lateinit var intent: Intent
    lateinit var uri : Uri


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val productView = LayoutInflater.from(parent.context).inflate(R.layout.sell_items,parent,false)
        return MyViewHolder(productView)
    }

    override fun onBindViewHolder(holder:MyViewHolder, position: Int) {
        val currentProduct: Sold=products[position]
       // uri = Uri.parse(currentProduct.imageURL)
        //holder.imageProduct.setImageURI(uri)
        holder.imageProduct.isVisible
        holder.name.text=currentProduct.name
        holder.price.text=currentProduct.price
        holder.quantity.text=currentProduct.quantity
        holder.Serialnum.text=currentProduct.Serialnum


        holder.EditButton.setOnClickListener{
            intent = Intent(holder.context, EditActivity::class.java)
            intent.putExtra("Name",currentProduct.name)
            intent.putExtra("Price",currentProduct.price)
            intent.putExtra("Quantity",currentProduct.quantity)
            intent.putExtra("Serial Number",currentProduct.Serialnum)
            holder.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return products.size
    }

    class MyViewHolder(productView : View) : RecyclerView.ViewHolder(productView){
        val imageProduct : ImageView = productView.findViewById(R.id.imageProduct)
        val name: TextView = productView.findViewById(R.id.name)
        val price: TextView = productView.findViewById(R.id.price)
        val quantity: TextView = productView.findViewById(R.id.quantity)
        val Serialnum: TextView = productView.findViewById(R.id.serialNum)
        val EditButton : Button = productView.findViewById(R.id.edit)
        var context: Context =productView.context
    }





}