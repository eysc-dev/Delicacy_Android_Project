package com.yschang.delicacy.viewmodel.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yschang.delicacy.MyApplication
import com.yschang.delicacy.MyApplication.Companion.usernamePreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class DetailViewModel : ViewModel() { /***** Food Description Screen *****/
    private val dao = MyApplication.database.userDao() // Access the user data access object

    init {

    }

    fun addToCart(food: MyApplication.Companion.Food) { //Add food to the cart
        viewModelScope.launch(Dispatchers.IO) {//Launche a coroutine in IO context
            val preferences = MyApplication.myDataStore.data.first() //Get the first value from the data store
            val username = preferences[usernamePreferences] //Retrieve the stored username
            val user = dao.findByUsername(username!!)
            val json = user?.cart
            if (json.isNullOrEmpty()) {
                val foodModel = FoodModel(listOf(food)) // Create a new FoodModel with the food item

                // Update the user with the new cart
                dao.updateUser(user?.copy(cart = Json.encodeToString(foodModel))!!)
            } else {
                val parseObject = Json.decodeFromString<FoodModel>(json)
                val foods = parseObject.foods.toMutableList()
                foods.add(food) // Add the new food item to the list

                val foodModel = FoodModel(foods) // Create a new FoodModel with the updated list
                // Update the user with the new cart
                dao.updateUser(user.copy(cart = Json.encodeToString(foodModel)))

            }
        }
    }
    @Serializable
    data class FoodModel( // Data class for representing a list of food items
        val foods: List<MyApplication.Companion.Food>
    )
}