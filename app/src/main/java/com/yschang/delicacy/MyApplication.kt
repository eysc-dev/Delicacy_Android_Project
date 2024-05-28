package com.yschang.delicacy

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.yschang.delicacy.database.AppDatabase
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class MyApplication : Application() {
    // Definition of a data storage repository DataStore, used for saving application preferences.
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    companion object {
        val loginPreferences = booleanPreferencesKey("isLogin") // Login status
        val usernamePreferences = stringPreferencesKey("username")
        val passwordPreferences = stringPreferencesKey("password")
        val emailPreferences = stringPreferencesKey("email")
        val foodPreferences = stringPreferencesKey("food")

        lateinit var database: AppDatabase // Reference to the Room database
        lateinit var myDataStore: DataStore<Preferences> // Reference to the DataStore instance

        // Define food
        lateinit var food: Food // The currently selected food.

        // Definition of the Food data class
        @Serializable
        data class Food(
            val name: String, // Food name
            val price: String, // Food price
            val image: String, // Food picture
            val description: String = "", // Food description
            var number: Int = 1 // Food quantity, default is 1
        )

        @Serializable
        data class Restaurant(
            val name: String,
            val image: String, // Image URL of the restaurant
            val foodTypes: List<FoodType>
        )

        @Serializable
        data class FoodType(
            val type: String,
            val foods: List<Food> // List of foods in this type
        )

        // List of restaurants with different food types
        val restaurants = listOf(
            Restaurant(
                "Take Away",
                "https://images.unsplash.com/photo-1489528792647-46ec39027556?q=80&w=2574&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                listOf(
                    FoodType(
                        "Main Dish",
                        listOf(
                            Food(
                                "Pork Hamburger",
                                "$0.01",
                                "https://plus.unsplash.com/premium_photo-1684534125392-e527ed4024e7?q=80&w=2671&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                                "A juicy pork patty, grilled to perfection, nestled between fresh, crisp lettuce and a warm, toasted bun."
                            ),
                            Food(
                                "Beef Hamburger",
                                "$6.99",
                                "https://upload.wikimedia.org/wikipedia/commons/thumb/c/ce/McDonald%27s_Quarter_Pounder_with_Cheese%2C_United_States.jpg/450px-McDonald%27s_Quarter_Pounder_with_Cheese%2C_United_States.jpg",
                                "Premium ground beef, seasoned and seared to a savory finish, topped with a slice of melting cheese on a soft bun."
                            ),
                            Food(
                                "French Fries with Cream Cheese",
                                "$6.99",
                                "https://images.unsplash.com/photo-1598679253544-2c97992403ea?q=80&w=2574&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                                "This dish features crispy French fries paired with creamy cream cheese."
                            ),
                            Food(
                                "Five Pieces of Chicken Wings",
                                "$7.99",
                                "https://images.unsplash.com/photo-1624153064067-566cae78993d?q=80&w=2574&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                                "Our crispy chicken wings are crafted to perfection using home-raised chicken and premium oil."
                            ),
                            Food(
                                "Hot Dog",
                                "$4.49",
                                "https://images.unsplash.com/photo-1613482084286-41f25b486fa2?q=80&w=2574&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                                "The most popular dish in our restaurant among customers"
                            ),
                            Food(
                                "Mexican Taco",
                                "$8.49",
                                "https://images.unsplash.com/photo-1627564803215-ad55bad5c5ea?q=80&w=2574&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                                "The Mexican Taco, crafted from 100% imported ingredients from Mexico, represents our latest culinary innovation"
                            ),
                            Food(
                                "Sandwich",
                                "$4.49",
                                "https://images.unsplash.com/photo-1626203050468-9308df7964fc?q=80&w=2681&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                                " Featuring bacon, mayonnaise, cucumber, and tomato, this dish is quickly prepared upon ordering, catering to the fast-paced schedules of working professionals and students."
                            ),
                        )
                    ),
                    FoodType(
                        "Drink",
                        listOf(
                            Food(
                                "Coca Cola",
                                "$4.49",
                                "https://images.unsplash.com/photo-1567103472667-6898f3a79cf2?q=80&w=2574&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
                            ),
                            Food(
                                "Schweppers",
                                "$3.49",
                                "https://images.unsplash.com/photo-1581006852262-e4307cf6283a?q=80&w=2574&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
                            ),
                            Food(
                                "Red Bull Energy Drink",
                                "$4.99",
                                "https://images.unsplash.com/photo-1632858280935-d5611683e434?q=80&w=2570&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
                            )
                        )
                    )
                )
            ),
            Restaurant(
                "Restorasa",
                "https://images.unsplash.com/photo-1707066158536-b7daa59a0315?q=80&w=2536&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                listOf(
                    FoodType(
                        "Main Dish",
                        listOf(
                            Food(
                                "Tomato Pasta",
                                "$10.49",
                                "https://images.unsplash.com/photo-1597131628347-c769fc631754?q=80&w=2670&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                                " Indulge in our authentic Italian-style spaghetti, topped with a savory tomato sauce and garnished with olives and fresh parsley."
                            ),
                            Food(
                                "Bacon Pasta with Cheese",
                                "$12.49",
                                "https://images.unsplash.com/photo-1579631542720-3a87824fff86?q=80&w=2564&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
                            ),
                            Food(
                                "Pepperoni and Olive Pizza",
                                "$10.99",
                                "https://images.unsplash.com/photo-1458642849426-cfb724f15ef7?q=80&w=2000&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                                "Our delectable pasta, featuring bacon, mozzarella cheese, and creamy sauce, is a perfect choice for those who love dairy products."
                            ),
                            Food(
                                "Neapolitan Pizza",
                                "$10.99",
                                "https://images.unsplash.com/photo-1574071318508-1cdbab80d002?q=80&w=2669&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                                "Our boss, who originates from Naples, shares his family's traditional Neapolitan pizza with food enthusiasts."
                            )
                        )
                    ),
                    FoodType(
                        "Soup",
                        listOf(
                            Food(
                                "Pumpkin Soup with Two Waffles",
                                "$6.99",
                                "https://plus.unsplash.com/premium_photo-1679503587085-df01c9ad5b1e?q=80&w=2574&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                                "For those with a sweet tooth like yourself..."
                            ),
                            Food(
                                "Borscht",
                                "$6.99",
                                "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a7/Borscht_served.jpg/330px-Borscht_served.jpg",
                                "For those who enjoy exploring unique flavors, try our local soup from Eastern Europe."
                            )
                        )
                    )
                )
            ),
            Restaurant(
                "Lotus Garden (Soft Opening)",
                "https://images.unsplash.com/photo-1668209313677-8cb1ae796de0?q=80&w=2534&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                listOf(
                    FoodType(
                        "Main Dish",
                        listOf(
                            Food(
                                "Roasted Chicken and Vegetables",
                                "$12.99",
                                "https://images.unsplash.com/photo-1518492104633-130d0cc84637?q=80&w=2526&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
                            ),
                            Food(
                                "Steak and French Fries",
                                "$17.99" ,
                                "https://images.unsplash.com/photo-1600891964092-4316c288032e?q=80&w=2670&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
                            )
                        )
                    )
                )
            ),
            Restaurant(
                "Rainbow Bubble Tea",
                "https://images.unsplash.com/photo-1628263967691-77d9e471fcb9?q=80&w=2670&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                listOf(
                    FoodType(
                        "Tea",
                        listOf(
                            Food(
                                "Brown Sugar Milk Bubble Tea",
                                "$7.99",
                                "https://images.unsplash.com/photo-1558857563-b371033873b8?q=80&w=2574&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                                "Brown Sugar Milk Bubble Tea is a delicious blend of milk and sweet brown sugar, enhanced with chewy tapioca pearls."
                            ),
                            Food(
                                "Mocha Milk Bubble Tea",
                                "$7.99",
                                "https://images.unsplash.com/photo-1637273484093-3e205aed2c59?q=80&w=2574&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                                "Mocha Milk Bubble Tea combines rich mocha flavor with milk, topped with chewy tapioca pearls."
                            ),
                            Food(
                                "Bubble Green Tea",
                                "$5.99",
                                "https://images.unsplash.com/photo-1627781245399-a1fe415c0046?q=80&w=2574&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                                " Refreshing green tea infused with chewy tapioca pearls for a delightful twist."
                            ),
                            Food(
                                "Oreo Milk Bubble Tea",
                                "$7.99",
                                "https://images.unsplash.com/photo-1600773407745-8eaa1937e802?q=80&w=2554&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                                "Enjoy creamy milk tea with crunchy Oreo cookies and chewy tapioca pearls. It's a deliciously decadent treat that's perfect for satisfying your sweet cravings with a refreshing twist.",
                            ),
                            Food(
                                "Strawberry Bubble Tea",
                                "$6.49",
                                "https://plus.unsplash.com/premium_photo-1664013499689-f2e00db87332?q=80&w=2670&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                                "Savor the sweetness of our Strawberry Bubble Tea, a harmonious blend of juicy strawberries and smooth tea, elevated by the fun chew of tapioca pearls. A refreshing delight in every sip, perfect for bubble tea lovers and newcomers alike."
                            )
                        )
                    )
                )
            ),
            Restaurant(
                " Po's House",
                "https://images.unsplash.com/photo-1562613498-8abe16e8373b?q=80&w=2574&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                listOf(
                    FoodType(
                        "Dumplings/Shumai",
                        listOf(
                            Food(
                                "Pork Dumplings",
                                "$7.99",
                                "https://plus.unsplash.com/premium_photo-1661602289442-be921f526ec0?q=80&w=2671&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                                "Made from handmade dumpling skins and Canadian sourced pork."
                            ),
                            Food(
                                "Chicken Dumplings",
                                "$7.99",
                                "https://plus.unsplash.com/premium_photo-1661602289442-be921f526ec0?q=80&w=2671&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                                "Made from handmade dumpling skins and Canadian sourced chicken."
                            ),
                            Food(
                                "Beef Dumplings",
                                "$8.49",
                                "https://plus.unsplash.com/premium_photo-1661602289442-be921f526ec0?q=80&w=2671&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                                "Made from handmade dumpling skins and Canadian sourced beef."
                            ),
                            Food(
                                "Shrimp Shumai",
                                "$8.49",
                                "https://plus.unsplash.com/premium_photo-1674601033631-79eeffaac6f9?q=80&w=2564&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                                "Experience the exquisite texture and flavor of Chinese traditional cuisine paired with fresh shrimp.",
                            )
                        )
                    ),
                    FoodType(
                        "Noodle",
                        listOf(
                            Food(
                                "Fried Instant Noodle",
                                "$5.99",
                                "https://images.unsplash.com/photo-1600326145359-3a44909d1a39?q=80&w=2564&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                                "Taiwanese night market favorites. Including half-cooked egg, cheese, and chopped scallions."
                            ),
                            Food(
                                "Chicken Noodle Soup",
                                "$8.99",
                                "https://images.unsplash.com/photo-1555126634-323283e090fa?q=80&w=2564&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                                "Spicy snail noodles with chicken, onions, bean sprouts, and cilantro."
                            ),
                            Food(
                                "Beef Noodle Soup",
                                "$11.49",
                                "https://images.unsplash.com/photo-1529690678884-189e81f34ef6?q=80&w=2670&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                                "Tender beef, drizzled with a 6-hour simmered beef broth, and garnished with scallions and seasonal vegetables, is the chef's favorite."
                            ),
                            Food(
                                "Shrimp Ramen",
                                "$10.99",
                                "https://images.unsplash.com/photo-1569718212165-3a8278d5f624?q=80&w=2680&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                                "Japanese culinary specialties. Including shrimps, soft-boiled egg, and seasonal vegetables."
                            ),
                            Food(
                                "Pork Ramen",
                                "$8.99",
                                "https://images.unsplash.com/photo-1557872943-16a5ac26437e?q=80&w=2631&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                                "Japanese culinary specialties. Including sliced pork, soft-boiled egg, nori, and premium soy sauce."
                            ),
                        )
                    ),
                    FoodType(
                        "Rice",
                        listOf(
                            Food(
                                "Rice Dumplings",
                                "$6.49",
                                "https://plus.unsplash.com/premium_photo-1661370008635-ed3a0148c3d9?q=80&w=2502&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                                "Traditional Chinese sticky rice dumplings wrapped in bamboo leaves, often filled with savory or ingredients."
                            ),
                            Food(
                                "Fried Rice with Shrimp and Chopped Scallions",
                                "$7.99",
                                "https://images.unsplash.com/photo-1609570324378-ec0c4c9b6ba8?q=80&w=2670&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                                "This dish is a must-order for nearly every family that frequents our restaurant."
                            ),
                            Food(
                                "Pork Rice",
                                "$8.49",
                                "https://images.unsplash.com/photo-1579619002916-88cd4c81a70c?q=80&w=2531&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                                "A freshly served bowl of Pork Loin Steaks on a bed of Kale and Lemon Rice"
                            ),
                            Food(
                                "Chicken Rice",
                                "$8.49",
                                "https://images.unsplash.com/photo-1599354607451-b02f5093d1fd?q=80&w=2574&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                                " Crafted with our special Thai sauce, this dish brings together the perfect balance of sweet, sour, and spicy flavors, paired with fragrant Thai rice, ideal for enthusiasts of Southeast Asian cuisine like yourself."
                            ),
                            Food(
                                "Braised Pork Rice",
                                "$6.49",
                                "https://images.unsplash.com/photo-1682496178083-74db4a32e473?q=80&w=2670&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                                "A beloved Taiwanese street food, Braised pork rice, served with a fried egg, cilantro, and seasonal vegetables."
                            ),
                            Food(
                                "Potato Rice",
                                "$7.99",
                                "https://images.unsplash.com/photo-1628521061262-19b5cdb7eee5?q=80&w=2535&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                                "Bento box with chicken, potatoes, and Thai rice."
                            )
                        )
                    )
                )
            )
        )

        val client = HttpClient(Android) {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                })
            }
        }
        lateinit var application: Application
        lateinit var currentFood: Food
        lateinit var currentRestaurant: Restaurant
    }

    // Lifecycle callback when the application is created
    override fun onCreate() {
        super.onCreate()
        application = this // Set the current application instance
        myDataStore = dataStore // Initialize the DataStore
        database = Room.databaseBuilder(
            applicationContext, // Current application context
            AppDatabase::class.java, "user" // Database class and name
        ).build() // Build the database
    }
}