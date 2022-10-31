package com.example.grocery_signaling

class User {

    private lateinit var FirstName: String
    private lateinit var LastName: String
    private lateinit var PhoneNumber: String
    private lateinit var Email: String
    private lateinit var Password: String
    constructor(Email: String, Password: String){
        this.Email=Email
        this.Password=Password
    }

    fun setFirstName(FirstName: String){
       this.FirstName=FirstName
    }
    fun setLastName(LastName: String){
        this.LastName=LastName
    }
    fun setPhoneNumber(PhoneNumber: String){
        this.PhoneNumber=PhoneNumber
    }
    override fun toString(): String {
        val data = this.FirstName + "" + this.LastName + "" + this.PhoneNumber +""
        return data
    }

}