package com.yschang.delicacy.viewmodel

import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paypal.android.corepayments.CoreConfig
import com.paypal.android.corepayments.Environment
import com.paypal.android.corepayments.PayPalSDKError
import com.paypal.android.paypalnativepayments.PayPalNativeCheckoutClient
import com.paypal.android.paypalnativepayments.PayPalNativeCheckoutListener
import com.paypal.android.paypalnativepayments.PayPalNativeCheckoutRequest
import com.paypal.android.paypalnativepayments.PayPalNativeCheckoutResult
import com.yschang.delicacy.MyApplication
import com.yschang.delicacy.viewmodel.home.DetailViewModel
import io.ktor.client.request.basicAuth
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.json.JSONObject
import java.math.BigDecimal
import java.math.RoundingMode

/*** For more info about Paypal api, plz see CartViewModel. ***/
class FoodDetailViewModel : ViewModel() { /***** Food Detail (Description) Screen *****/
    private val dao = MyApplication.database.userDao()
    private val context = MyApplication.application

    // PayPal client ID and secret for authentication with the PayPal API
    private val id =
        "ATe7oRfpWoC5taYNXVpjh7RDZc3afxEodTshL0QDaWErG0YX1yHZlaXsfkTwwl6PC79sUkkSziWvUGAr"
    private val secret =
        "EKpcleSJnxNVLJ988BwcPWykNAuHjQiPQgbSuVKyRDBVsRVD-Tcgl6Cxo3prIWMaEBfATXG4Df7M9FVK"
    val state = MutableStateFlow(State())

    init { //Initialize the ViewModel to check if the current food is a favorite
        viewModelScope.launch(Dispatchers.IO) {
            state.update {
                it.copy(
                    isFavorite = false,
                    isLoading = true
                )
            }
            // Get the current username from DataStore
            val preferences = MyApplication.myDataStore.data.first()
            val username = preferences[MyApplication.usernamePreferences]

            // Retrieve the user from the database
            val user = dao.findByUsername(username!!)
            val json = user?.favorite
            if (json.isNullOrEmpty()) {
                state.update {
                    it.copy(
                        isFavorite = false,
                        isLoading = false
                    )
                }
            } else { // If there's data, check if the current food is in the favorite list
                val parseObject = Json.decodeFromString<DetailViewModel.FoodModel>(json)
                val foods = parseObject.foods
                var isFavorite = false

                foods.forEach {// Check each food item in the list
                    if (it.name == MyApplication.currentFood.name) {
                        isFavorite = true
                        return@forEach // Exit the loop if found
                    }
                }
                state.update {
                    it.copy(
                        isFavorite = isFavorite,
                        isLoading = false
                    )
                }
            }
        }
    }

    // Toggle the favorite status of the current food item
    fun favorite() {
        viewModelScope.launch(Dispatchers.IO) {
            val preferences = MyApplication.myDataStore.data.first()
            val username = preferences[MyApplication.usernamePreferences]
            var user = dao.findByUsername(username!!)
            var json = user?.favorite

            //Initialize the Favorite List //If there's no food in a favorite list, create a new one
            if (json.isNullOrEmpty()) {
                val foodModel = DetailViewModel.FoodModel(
                    listOf(
                        MyApplication.currentFood
                    )
                )
                dao.updateUser( // Update the user with the new favorite list
                    user?.copy(favorite = Json.encodeToString(foodModel))!!
                )
            } else { // If there's existing food in a favorite list, updating the existing Favorite List
                val parseObject = Json.decodeFromString<DetailViewModel.FoodModel>(json)
                val foods = parseObject.foods.toMutableList()
                var isFind = false

                // Check if the current food is in the list, if so, remove it
                foods.forEach {
                    if (it.name == MyApplication.currentFood.name) {
                        println("find")
                        foods.remove(it)
                        isFind = true
                        return@forEach
                    }
                }
                if (!isFind) { // If not found, add it to the list
                    foods.add(MyApplication.currentFood)
                }
                val foodModel = DetailViewModel.FoodModel(foods)
                dao.updateUser(
                    user?.copy(
                        favorite = Json.encodeToString(foodModel)
                    )!!
                )
            }
            // Re-fetch the user and update the favorite status
            user = dao.findByUsername(username)
            json = user?.favorite

            // Check if the current food is in the list, if so, it's a favorite
            if (json?.isEmpty() == true) { // If there's no data, not a favorite
                state.update {
                    it.copy(
                        isFavorite = false,
                        isLoading = false
                    )
                }
            } else {
                val parseObject = Json.decodeFromString<DetailViewModel.FoodModel>(json!!)
                val foods = parseObject.foods
                var isFavorite = false


                foods.forEach {
                    if (it.name == MyApplication.currentFood.name) {
                        isFavorite = true
                        return@forEach
                    }
                }
                state.update {
                    it.copy(
                        isFavorite = isFavorite,
                        isLoading = false
                    )
                }
            }
        }
    }

    //https://developer.android.com/reference/android/widget/Toast
    //https://developer.android.com/guide/topics/ui/notifiers/toasts

    // Add the current food item to the shopping cart
    fun cart(food: MyApplication.Companion.Food) {
        println(food)
        Toast.makeText(context, "Added to cart", Toast.LENGTH_SHORT).show()
        viewModelScope.launch(Dispatchers.IO) {
            // Retrieve the current user from the database
            val preferences = MyApplication.myDataStore.data.first()
            val username = preferences[MyApplication.usernamePreferences]
            val user = dao.findByUsername(username!!)
            val json = user?.cart

            if (json.isNullOrEmpty()) { // If there's no cart data, create a new list with the current food
                val foodModel = DetailViewModel.FoodModel(
                    listOf(food) // Add the current food to the cart
                )
                dao.updateUser(
                    user?.copy(
                        cart = Json.encodeToString(foodModel)
                    )!!
                )
            } else { // If there's existing cart data, update it
                val parseObject = Json.decodeFromString<DetailViewModel.FoodModel>(json)
                val foods = parseObject.foods.toMutableList()
                var isFind = false

                // Check if the current food is in the list, if so, increment the quantity
                foods.forEach {
                    if (it.name == food.name) {
                        println("find")
                        it.number += 1
                        println("number: ${it.number}")
                        isFind = true
                        return@forEach
                    }
                }
                if (!isFind) { // If not found, add it to the cart
                    foods.add(food)
                }
                val foodModel = DetailViewModel.FoodModel(foods)
                dao.updateUser(
                    user.copy(
                        cart = Json.encodeToString(foodModel)
                    )
                )
            }
            // Update the shared state with the updated cart information
            SharedState.state.update {
                it.copy(isLoading = true)
            }
            if (json.isNullOrEmpty()) { // If there's no cart data, update with empty list
                SharedState.state.update {
                    it.copy(
                        isLoading = false,
                        cart = emptyList()
                    )
                }
            } else { // If there's cart data, calculate the total price and update the state
                val parseObject = Json.decodeFromString<DetailViewModel.FoodModel>(json)
                val foods = parseObject.foods
                var total = BigDecimal.ZERO // Initialize the total price to zero

                foods.forEach {// Calculate the total price of the cart
                    var price = it.price
                    price = price.replace("$", "")
                    println("price: ${price.toDouble()}")
                    total =
                        total.add(BigDecimal(price.toDouble()))
                            .setScale(2, RoundingMode.HALF_UP)
                }
                SharedState.state.update {
                    it.copy(
                        isLoading = false,
                        cart = foods,
                        total = total
                    )
                }
            }
        }
    }

    fun buy() {  // Starts the PayPal checkout process for the current food item
        viewModelScope.launch(Dispatchers.IO) {
            val coreConfig = CoreConfig(
                id, // Client ID
                environment = Environment.LIVE
            )
            // Initialize the PayPalNativeCheckoutClient with the app context, CoreConfig, and return URL
            val payPalNativeClient = PayPalNativeCheckoutClient(
                application = MyApplication.application,
                coreConfig = coreConfig,
                returnUrl = "com.yschang.delicacy://paypalpay"
            )

            // Set up a listener to handle PayPal checkout events
            payPalNativeClient.listener = object : PayPalNativeCheckoutListener {
                override fun onPayPalCheckoutStart() {
                    Toast.makeText( // Notify that the PayPal checkout has started
                        MyApplication.application,
                        "PayPalCheckoutStart",
                        Toast.LENGTH_SHORT
                    ).show()
                    println("PayPalCheckoutStart")
                }

                override fun onPayPalCheckoutSuccess(result: PayPalNativeCheckoutResult) {
                    Toast.makeText( // Notify that the PayPal checkout was successful
                        MyApplication.application,
                        "PayPalCheckoutSuccess",
                        Toast.LENGTH_SHORT
                    ).show()
                    println("PayPalCheckoutSuccess")
                    viewModelScope.launch(Dispatchers.IO) {
                        val preferences = MyApplication.myDataStore.data.first()
                        val username = preferences[MyApplication.usernamePreferences]
                        val user = dao.findByUsername(username!!)
                        val json = user?.history
                        println("json: $json")
                        if (json.isNullOrEmpty()) { //If there's no purchase history, add the current food as a new purchase
                            println("empty")
                            val foodModel = DetailViewModel.FoodModel(
                                listOf(MyApplication.currentFood) //Add the current food to the purchase history
                            )
                            dao.updateUser(
                                user?.copy(
                                    history = Json.encodeToString(foodModel)
                                )!!
                            )
                        } else { // If there's existing purchase history, update it
                            val parseObject = Json.decodeFromString<DetailViewModel.FoodModel>(json)
                            val foods = parseObject.foods.toMutableList()
                            var isFind = false

                            // Check if the current food is in the list, if so, increment the quantity
                            foods.forEach {
                                if (it.name == MyApplication.currentFood.name) {
                                    println("find")
                                    it.number += 1
                                    println("number: ${it.number}")
                                    isFind = true
                                    return@forEach
                                }
                            }
                            if (!isFind) {
                                foods.add(
                                    MyApplication.currentFood
                                )
                            }
                            val foodModel = DetailViewModel.FoodModel(
                                foods
                            )
                            dao.updateUser(
                                user.copy(
                                    history = Json.encodeToString(foodModel)
                                )
                            )
                        }
                    }
                }

                override fun onPayPalCheckoutFailure(error: PayPalSDKError) {
                    Toast.makeText(
                        MyApplication.application,
                        "onPayPalCheckoutFailure",
                        Toast.LENGTH_SHORT
                    ).show()
                    println("PayPalCheckoutFailure ${error.errorDescription}")
                }

                override fun onPayPalCheckoutCanceled() {
                    Toast.makeText(
                        MyApplication.application,
                        "PayPalCheckoutCanceled",
                        Toast.LENGTH_SHORT
                    ).show()
                    println("PayPalCheckoutCanceled")
                }
            }
            // Get an OAuth2 token from PayPal
            val tokenRequest =
                MyApplication.client.post("https://api-m.paypal.com/v1/oauth2/token") {
                    basicAuth(id, secret)
                    setBody(
                        "grant_type=client_credentials"
                    )
                }
            val token = JSONObject(tokenRequest.bodyAsText()).getString("access_token")
            // Create a new order request to start the checkout process
            val orderRequest =
                MyApplication.client.post("https://api-m.paypal.com/v2/checkout/orders") {
                    contentType(ContentType.Application.Json)
                    basicAuth(id, secret)
                    setBody(
                        """
                    {
                        "intent": "CAPTURE",
                        "purchase_units": [
                            {
                                "amount": {
                                    "currency_code": "USD",
                                    "value": ${MyApplication.currentFood.price.replace("$", "")}
                                }
                            }
                        ]
                    }
                """.trimIndent()
                    )
                }
            println(orderRequest.bodyAsText())

            // Extract the order ID from the response
            val orderId = JSONObject(orderRequest.bodyAsText()).getString("id")
            println("orderId: $orderId")

            // Create a checkout request with the order ID.
            val request = PayPalNativeCheckoutRequest(orderId)
            payPalNativeClient.startCheckout(request) // Start the PayPal checkout process
        }
    }

    fun testAdd() {

    }

    data class State(
        val isFavorite: Boolean = false, // Indicates if the food is a favorite
        val isLoading: Boolean = false // Indicates if loading is in progress
    )
}