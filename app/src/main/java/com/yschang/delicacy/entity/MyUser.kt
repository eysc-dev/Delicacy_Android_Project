package com.yschang.delicacy.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//Defines an entity class 'MyUser' that represents a table in the Room database.
@Entity
data class MyUser(
    @PrimaryKey(autoGenerate = true)
    val id: Int, //The user's ID

    val username: String,
    val fullName:String, //The user's full name.
    val phoneNumber:String,
    val password: String,
    val email: String, //The user's email
    val code:String, // The verification code sent to the user's email
    val cart:String="", //The user's shopping cart, defaulting to an empty string.
    val history:String="", //The user's purchase history, defaulting to an empty string.
    val favorite:String="" //The user's favorites, defaulting to an empty string.
)
