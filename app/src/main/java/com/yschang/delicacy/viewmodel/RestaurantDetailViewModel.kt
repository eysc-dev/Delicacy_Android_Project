package com.yschang.delicacy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yschang.delicacy.MyApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RestaurantDetailViewModel : ViewModel() { /***** List of Dishes in the Restaurant Screen *****/
    val state = MutableStateFlow(State())
    private var isDesc = false // Flag to determine sort order for food items
    private var isDrinkDesc = false // Flag to determine sort order for drink items

    init { // Initialize the state with food, drink, and other lists types from the current restaurant
        state.update {
            it.copy(
                foodList =
                MyApplication.currentRestaurant.foodTypes[0].foods.ifEmpty { emptyList() },
                drinkList =
                if (MyApplication.currentRestaurant.foodTypes.size > 1)
                    MyApplication.currentRestaurant.foodTypes[1].foods.ifEmpty { emptyList() }
                else emptyList(),
                otherList = if (MyApplication.currentRestaurant.foodTypes.size > 2)
                    MyApplication.currentRestaurant.foodTypes[2].foods.ifEmpty { emptyList() }
                else emptyList()
            )
        }
    }

    fun sortFood() { //Sort food items by price
        viewModelScope.launch(Dispatchers.IO) {
            state.update { state1 ->
                state1.copy(
                    foodList =
                    if (isDesc) // Check if food price descending order
                        MyApplication.currentRestaurant.foodTypes[0].foods.sortedByDescending {
                            it.price // Sort by descending price
                        }
                    else
                        MyApplication.currentRestaurant.foodTypes[0].foods.sortedBy {
                            it.price // Sort by ascending price
                        }
                )
            }
            isDesc = !isDesc
        }
    }

    fun sortDrink() { //Sort drink items by price
        viewModelScope.launch(Dispatchers.IO) {
            state.update { state1 ->
                state1.copy(
                    drinkList =
                    if (isDrinkDesc)
                        MyApplication.currentRestaurant.foodTypes[1].foods.sortedByDescending {
                            it.price
                        }
                    else
                        MyApplication.currentRestaurant.foodTypes[1].foods.sortedBy {
                            it.price
                        }
                )
            }
            isDrinkDesc = !isDrinkDesc // Toggle the sort order flag
        }
    }

    fun sortOther() { //Sort other types by price
        viewModelScope.launch(Dispatchers.IO) {
            state.update { state1 ->
                state1.copy(
                    otherList =
                    if (isDesc)
                        MyApplication.currentRestaurant.foodTypes[2].foods.sortedByDescending {
                            it.price
                        }
                    else
                        MyApplication.currentRestaurant.foodTypes[2].foods.sortedBy {
                            it.price
                        }
                )
            }
            isDesc = !isDesc
        }
    }

    data class State(
        val foodList: List<MyApplication.Companion.Food> = emptyList(),
        val drinkList: List<MyApplication.Companion.Food> = emptyList(),
        val otherList: List<MyApplication.Companion.Food> = emptyList(),
        val isLoading: Boolean = false // Track if data is being loaded
        /*跟蹤isLoading有助於確保應用程序的界面在操作進行時提供足夠的反饋，
        這樣用戶就知道他們需要等待以及應用程序在做什麼。這也是增強用戶體驗的一個重要方面*/
    )
}