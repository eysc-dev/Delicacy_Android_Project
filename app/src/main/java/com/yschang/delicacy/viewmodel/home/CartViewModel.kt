package com.yschang.delicacy.viewmodel.home

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
import com.yschang.delicacy.viewmodel.SharedState
import io.ktor.client.request.basicAuth
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.json.JSONObject
import java.math.BigDecimal
import java.math.RoundingMode

class CartViewModel : ViewModel() { /***** Shopping Cart Screen *****/

    private val dao = MyApplication.database.userDao() // Access dao
    private val dataStore = MyApplication.myDataStore // Access data store

    //https://developer.paypal.com/dashboard/applications/edit/QVVIQVh3VnJhbDNFTEVwemRxRE1paWpUY2x6OEY2YWxnRHZKM1NGQ05va0FDVVMyYV81cUR4eDJ0Tlh2eDIwTTM5VnFHX0s0c05EaGRsd0M=?accountWebhooks=true
    // Client ID & Secret key 1 for a business account
    private val id =
        "ATe7oRfpWoC5taYNXVpjh7RDZc3afxEodTshL0QDaWErG0YX1yHZlaXsfkTwwl6PC79sUkkSziWvUGAr"
    private val secret =
        "EKpcleSJnxNVLJ988BwcPWykNAuHjQiPQgbSuVKyRDBVsRVD-Tcgl6Cxo3prIWMaEBfATXG4Df7M9FVK"

    init {
        refresh() // Refresh the cart data when the ViewModel is initialized
    }

    /*fun testHistory() { // JUST FOR TEST !!!!!!!!!!
        viewModelScope.launch(Dispatchers.IO) {//Launche a coroutine in IO context
            val preferences = dataStore.data.first() // Get the data store's first value
            val username = preferences[MyApplication.usernamePreferences] //Retrieve the stored username
            val user = dao.findByUsername(username!!) //Find the user by username
            val cart = user?.cart // Get the cart from the user
            val history = user?.history // Get the history from the user

            if (history.isNullOrEmpty().not()) { // Check if history is not empty
                // Decode the cart JSON
                val cartObj = Json.decodeFromString<DetailViewModel.FoodModel>(cart!!)
                val historyObj =
                    Json.decodeFromString<DetailViewModel.FoodModel>(history!!) //Decode the history JSON
                val foods = historyObj.foods.toMutableList() //Convert the history to a mutable list
                cartObj.foods.forEach { cartIndex -> // oops through the history of purchased food
                    historyObj.foods.forEach { historyIndex ->
                        if (cartIndex.name == historyIndex.name) { // If names match
                            val newFood = historyIndex.copy(
                                number = historyIndex.number + cartIndex.number //Merge quantities
                            )
                            foods.remove(historyIndex) // Remove the old item
                            foods.add(newFood) // Add the updated item
                        } else { // If names do not match
                            foods.add(cartIndex) // Add the cart item to history
                        }
                    }
                }
                // Create a new food model with the updated list
                val foodModel = DetailViewModel.FoodModel(foods)
                // Update the user
                dao.updateUser(
                    user.copy(
                        cart = "",
                        history = Json.encodeToString(foodModel)
                    )
                )
            }

        }
    }*/

    fun plus(index: Int) { //Increase the quantity of an item in the cart
        viewModelScope.launch(Dispatchers.IO) {//Launche a coroutine in IO context
            SharedState.state.update {
                it.copy(isLoading = true) //Set loading state to true
            }
            val preferences = dataStore.data.first() //Get the data store's first value
            val username = preferences[MyApplication.usernamePreferences] //Retrieve the stored username
            val user = dao.findByUsername(username!!) // Find the user by username
            val json = user?.cart // Get the cart as a JSON string
            val parseObject = Json.decodeFromString<DetailViewModel.FoodModel>(json!!) //Decode the cart JSON
            val foods = parseObject.foods.toMutableList() // Convert the foods to a mutable list
            val food = foods[index] // Get the specific food item
            val newFood = food.copy(number = food.number + 1) // Increase the quantity
            foods[index] = newFood // Update the food item in the list

            val foodModel = DetailViewModel.FoodModel(
                foods // Create a new food model with the updated list
            )
            // Update the user with the new cart
            dao.updateUser(user.copy(cart = Json.encodeToString(foodModel)))

            refresh() // Refresh the cart data
        }
    }

    fun minus(index: Int) { //Decrease the quantity of an item in the cart
        viewModelScope.launch(Dispatchers.IO) {
            SharedState.state.update {
                it.copy(isLoading = true)
            }
            val preferences = dataStore.data.first() // Get the data store's first value
            val username = preferences[MyApplication.usernamePreferences] //Retrieve the stored username
            val user = dao.findByUsername(username!!)
            val json = user?.cart
            val parseObject = Json.decodeFromString<DetailViewModel.FoodModel>(json!!)
            val foods = parseObject.foods.toMutableList()
            val food = foods[index] // Get the specific food item
            if (food.number == 1) { // If the quantity is one
                foods.removeAt(index) // Remove the item from the cart
            } else { // If the quantity is greater than one
                val newFood = food.copy(
                    number = food.number - 1 // Decrease the quantity
                )
                foods[index] = newFood // Update the food item in the list
            }
            // Creates a new food model with the updated list
            val foodModel = DetailViewModel.FoodModel(foods)
            dao.updateUser( // Update the user with the new cart
                user.copy(
                    cart = Json.encodeToString(foodModel)
                )
            )
            refresh()
        }
    }

    fun refresh() { //Refresh the cart data
        viewModelScope.launch(Dispatchers.IO) {
            SharedState.state.update {
                it.copy(isLoading = true)
            }
            val preferences = dataStore.data.first()
            val username = preferences[MyApplication.usernamePreferences]
            val user = dao.findByUsername(username!!) // Find the user by username
            val json = user?.cart // Get the cart as a JSON string
            println("json: $json")
            if (json.isNullOrEmpty()) { // If the cart is empty or null
                SharedState.state.update {
                    it.copy(isLoading = false, cart = emptyList()) // Set the cart to empty
                }
            } else {
                val parseObject = Json.decodeFromString<DetailViewModel.FoodModel>(json) //Decode the cart JSON
                val foods = parseObject.foods // Get the food list
                var total = BigDecimal.ZERO // Initialize the total amount
                foods.forEach {
                    val number = it.number // Get the quantity
                    var price = it.price // Get the price
                    price = price.replace("$", "") // Remove the dollar sign
                    println("price: ${price.toDouble()}") //for debugging
                    total =
                        total.add( // Add the price multiplied by the quantity to the total
                            BigDecimal(price.toDouble()).multiply(BigDecimal(number))
                        ).setScale(2, RoundingMode.HALF_UP) // Round to two decimal places
                }
                SharedState.state.update {// Update the shared state with the new cart details
                    it.copy(
                        isLoading = false, // Set loading to false
                        cart = foods, // Set the cart with the food list
                        total = total // Set the total amount
                    )
                }
            }
        }
    }

    fun delete(index: Int) { //Delete food item from the cart
        viewModelScope.launch(Dispatchers.IO) {
            SharedState.state.update {
                it.copy(isLoading = true)
            }
            val preferences = dataStore.data.first()
            val username = preferences[MyApplication.usernamePreferences]
            val user = dao.findByUsername(username!!)
            val json = user?.cart
            val parseObject = Json.decodeFromString<DetailViewModel.FoodModel>(json!!)
            val foods = parseObject.foods.toMutableList()
            foods.removeAt(index) // Remove the specific item from the list

            // Create a new food model with the updated list
            val foodModel = DetailViewModel.FoodModel(foods)
            // Update the user with the new cart
            dao.updateUser(user.copy(cart = Json.encodeToString(foodModel)))
            refresh() // Refresh the cart data
        }
    }

    //ref: https://github.com/paypal/paypal-android/blob/main/MOBILE_CHECKOUT_MIGRATION_GUIDE.md
    fun checkout() { //PayPal Checkout
        viewModelScope.launch(Dispatchers.IO) {
            // Configures PayPal
            val coreConfig = CoreConfig(
                id, //Client ID for PayPal service
                //Specify the environment as the actual payment environment(LIVE)
                environment = Environment.LIVE
            )
            // Creates a PayPal native checkout client
            val payPalNativeClient = PayPalNativeCheckoutClient(
                application = MyApplication.application,
                coreConfig = coreConfig,
                returnUrl = "com.yschang.delicacy://paypalpay"
            )

            //https://developer.android.com/reference/android/widget/Toast
            //https://developer.android.com/guide/topics/ui/notifiers/toasts
            payPalNativeClient.listener = object : PayPalNativeCheckoutListener {
                override fun onPayPalCheckoutStart() { // When PayPal checkout starts
                    Toast.makeText(
                        MyApplication.application,
                        "onPayPalCheckoutStart",
                        Toast.LENGTH_SHORT
                    ).show()
                    println("PayPalCheckoutStart")
                }

                // Display a success prompt and process subsequent user data updates
                // When checkout is successful
                override fun onPayPalCheckoutSuccess(result: PayPalNativeCheckoutResult) {
                    Toast.makeText(
                        MyApplication.application,
                        "onPayPalCheckoutSuccess",
                        Toast.LENGTH_SHORT
                    ).show() // Shows a toast

                    // Handle database operations
                    // Launche a coroutine to update user data
                    viewModelScope.launch(Dispatchers.IO) {
                        val preferences = dataStore.data.first()
                        val username = preferences[MyApplication.usernamePreferences]
                        val user = dao.findByUsername(username!!)
                        val cart = user?.cart
                        val history = user?.history // Get the history as a JSON string
                        if (history.isNullOrEmpty().not()) { // If the history is not empty
                            val cartObj = Json.decodeFromString<DetailViewModel.FoodModel>(cart!!)
                            val historyObj =
                                Json.decodeFromString<DetailViewModel.FoodModel>(history!!)
                            val foods = historyObj.foods.toMutableList()
                            cartObj.foods.forEach { cartIndex -> // Loops through cart items
                                historyObj.foods.forEach { historyIndex -> // Loops through history items
                                    if (cartIndex.name == historyIndex.name) { // If names match
                                        val newFood = historyIndex.copy( // Merge quantities
                                            number = historyIndex.number + cartIndex.number
                                        )
                                        foods.remove(historyIndex) // Removes the old item
                                        foods.add(newFood) // Adds the updated item
                                    } else { // If names do not match
                                        foods.add(cartIndex) // Adds the cart item to history
                                    }
                                }
                            }
                            // Create a new food model with the updated list
                            val foodModel = DetailViewModel.FoodModel(foods)
                            dao.updateUser( // Update the user
                                user.copy(
                                    cart = "",
                                    history = Json.encodeToString(foodModel)
                                )
                            )
                        }

                    }
                }

                override fun onPayPalCheckoutFailure(error: PayPalSDKError){ //When checkout fails
                    Toast.makeText(
                        MyApplication.application,
                        "onPayPalCheckoutFailure",
                        Toast.LENGTH_SHORT
                    ).show()
                    println("PayPalCheckoutFailure ${error.errorDescription}")
                }

                override fun onPayPalCheckoutCanceled(){ // When checkout is canceled
                    Toast.makeText(
                        MyApplication.application,
                        "PayPalCheckoutCanceled",
                        Toast.LENGTH_SHORT
                    ).show()
                    println("PayPalCheckoutCanceled")
                }
            }

            // Send a POST request to get an OAuth2 token
            val tokenRequest = // the paypal live endpoint
                MyApplication.client.post("https://api-m.paypal.com/v1/oauth2/token") {
                    basicAuth(id, secret)
                    setBody(
                        "grant_type=client_credentials"
                    )
                }
            // Sending requests to the PayPal API
            val token = JSONObject(tokenRequest.bodyAsText()).getString("access_token")
            val orderRequest = // the paypal live endpoint

                //https://developer.paypal.com/docs/api/payments/v2/
                MyApplication.client.post("https://api-m.paypal.com/v2/checkout/orders") {
                    contentType(ContentType.Application.Json) // Set the content type to JSON
                    basicAuth(id, secret)

                    //Request Sample - cURL
                    // "intent": "CAPTURE" => capture the funds immediately
                    // ${SharedState.state.value.total} => retrieve the total amount from the shopping cart
                    setBody(
                        """
                    {
                        "intent": "CAPTURE",
                        "purchase_units": [
                            {
                                "amount": {
                                    "currency_code": "USD",
                                    "value": ${SharedState.state.value.total}
                                }
                            }
                        ]
                    }
                """.trimIndent() //Remove leading whitespace from each line of the string
                    )
                }
            /**************************************************************/
            /**************************************************************/

            // Extract the order ID from the response
            val orderId = JSONObject(orderRequest.bodyAsText()).getString("id")
            println("orderId: $orderId")
            // Create a checkout request with the order ID
            val request = PayPalNativeCheckoutRequest(orderId)
            payPalNativeClient.startCheckout(request) // Start the PayPal checkout process
        }
    }

    data class CartState( // Data class for the cart
        val isLoading: Boolean = false, // Indicate if the cart is loading
        val cart: List<MyApplication.Companion.Food> = emptyList(), //The list of food items in the cart
        val total: BigDecimal = BigDecimal.ZERO //The total amount of the cart
    )
}