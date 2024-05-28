package com.yschang.delicacy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yschang.delicacy.MyApplication
import com.yschang.delicacy.viewmodel.home.DetailViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class HistoryViewModel:ViewModel() { /***** Purchase History Screen that I didn't Use *****/
    val dao = MyApplication.database.userDao()
    val state = MutableStateFlow(HistoryState())
    init {
        refresh()
    }
    fun refresh(){
        viewModelScope.launch(Dispatchers.IO){
            state.value = state.value.copy(isLoading = true)
            val preferences = MyApplication.myDataStore.data.first()
            val username = preferences[MyApplication.usernamePreferences]
            val user = dao.findByUsername(username!!)
            val json = user?.history
            println(json)
            if(!json.isNullOrEmpty()){
                val parseObject = Json.decodeFromString<DetailViewModel.FoodModel>(json)
                state.value = state.value.copy(cart = parseObject.foods)
            }
        }
    }
    data class HistoryState(
        val isLoading: Boolean = false, // Whether the screen is currently loading
        val cart: List<MyApplication.Companion.Food> = emptyList(), // List of food items in the history
    )
}