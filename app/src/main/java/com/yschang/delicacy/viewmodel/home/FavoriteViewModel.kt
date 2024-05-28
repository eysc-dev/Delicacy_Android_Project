package com.yschang.delicacy.viewmodel.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yschang.delicacy.MyApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class FavoriteViewModel : ViewModel() { /***** Favorite Screen *****/
    val state = MutableStateFlow(State())
    private val dao = MyApplication.database.userDao() //DAO for accessing the database (for user data)
    private val dataStore = MyApplication.myDataStore //DataStore for persisting simple key-value data

    init {
        refresh() //Refresh the favorite list when the ViewModel is initialized
    }

    fun refresh() { // Refresh the list of favorite items
        viewModelScope.launch(Dispatchers.IO) {
            state.update {// Update the state to indicate loading
                it.copy(isLoading = true)
            }
            val preferences = dataStore.data.first()
            // Retrieve the username from DataStore
            val username = preferences[MyApplication.usernamePreferences]
            val user = dao.findByUsername(username!!) // Find the user in the database
            val json = user?.favorite // Get the JSON string of favorite items from the user data
            println("json: $json")

            if (json.isNullOrEmpty()) { //If the JSON is null or empty
                state.update {// Update the state with an empty list if there's no data
                    it.copy(
                        isLoading = false,
                        favoriteList = emptyList()
                    )
                }
            } else { //If there is food in the Favorite list
                // Parse the JSON string into a FoodModel
                val parseObject = Json.decodeFromString<DetailViewModel.FoodModel>(json)
                val foods = parseObject.foods // Retrieve the list of favorite foods
                state.update {
                    it.copy(
                        isLoading = false,
                        favoriteList = foods,
                    )
                }
            }
        }
    }

    // Remove an item from the favorite list
    fun unFavorite(food: MyApplication.Companion.Food) {
        viewModelScope.launch(Dispatchers.IO) {
            val preferences = dataStore.data.first()
            val username = preferences[MyApplication.usernamePreferences]
            val user = dao.findByUsername(username!!)
            val json = user?.favorite
            if (json.isNullOrEmpty()) {
                return@launch
            } else {
                val parseObject = Json.decodeFromString<DetailViewModel.FoodModel>(json)
                val foods = parseObject.foods.toMutableList()
                foods.remove(food) // Remove the specified food item from the list

                // Create a new FoodModel with the updated list
                val foodModel = DetailViewModel.FoodModel(foods)

                // Update the user in the database with the new favorite list
                dao.updateUser(
                    user.copy(
                        favorite = Json.encodeToString(foodModel)
                    )
                )
                refresh()
            }
        }
    }
    data class State(
        val favoriteList: List<MyApplication.Companion.Food> = emptyList(),
        val isLoading: Boolean = false
    )
}