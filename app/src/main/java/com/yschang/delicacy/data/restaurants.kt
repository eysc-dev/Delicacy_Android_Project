/*package com.yschang.delicacy.data

import com.yschang.delicacy.MyApplication*/

/********** Did Not Use This .kt File **********/
/********** Did Not Use This .kt File **********/

/*
object Restaurants {

    val restaurants = listOf(
        MyApplication.Companion.Restaurant(
            "Take Away",
            "https://images.unsplash.com/photo-1489528792647-46ec39027556?q=80&w=2574&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            listOf(
                MyApplication.Companion.FoodType(
                    "Main Dish",
                    listOf(
                        MyApplication.Companion.Food(
                            "Pork Hamburger",
                            "$5.99",
                            "https://plus.unsplash.com/premium_photo-1684534125392-e527ed4024e7?q=80&w=2671&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                            "A juicy pork patty, grilled to perfection, nestled between fresh, crisp lettuce and a warm, toasted bun."
                        ),
                        MyApplication.Companion.Food(
                            "Beef Hamburger",
                            "$6.99",
                            "https://upload.wikimedia.org/wikipedia/commons/thumb/c/ce/McDonald%27s_Quarter_Pounder_with_Cheese%2C_United_States.jpg/450px-McDonald%27s_Quarter_Pounder_with_Cheese%2C_United_States.jpg",
                            "Premium ground beef, seasoned and seared to a savory finish, topped with a slice of melting cheese on a soft bun."
                        ),
                        MyApplication.Companion.Food(
                            "French Fries with Cream Cheese",
                            "$6.99",
                            "https://images.unsplash.com/photo-1598679253544-2c97992403ea?q=80&w=2574&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                            "This dish features crispy French fries paired with creamy cream cheese."
                        ),
                        MyApplication.Companion.Food(
                            "Five Pieces of Chicken Wings",
                            "$7.99",
                            "https://images.unsplash.com/photo-1624153064067-566cae78993d?q=80&w=2574&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                            "Our crispy chicken wings are crafted to perfection using home-raised chicken and premium oil."
                        ),
                        MyApplication.Companion.Food(
                            "Hot Dog",
                            "$4.49",
                            "https://images.unsplash.com/photo-1613482084286-41f25b486fa2?q=80&w=2574&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                            "The most popular dish in our restaurant among customers."
                        ),
                        MyApplication.Companion.Food(
                            "Taco",
                            "$8.49",
                            "https://images.unsplash.com/photo-1627564803215-ad55bad5c5ea?q=80&w=2574&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                            "The Mexican Taco, crafted from 100% imported ingredients from Mexico, represents our latest culinary innovation."
                        ),
                        MyApplication.Companion.Food(
                            "Sandwich",
                            "$4.49",
                            "https://images.unsplash.com/photo-1626203050468-9308df7964fc?q=80&w=2681&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                            "Featuring bacon, mayonnaise, cucumber, and tomato, this dish is quickly prepared upon ordering, catering to the fast-paced schedules of working professionals and students."
                        ),
                    )
                ),
                MyApplication.Companion.FoodType(
                    "Drink",
                    listOf(
                        MyApplication.Companion.Food(
                            "Coca Cola",
                            "$4.49",
                            "https://images.unsplash.com/photo-1567103472667-6898f3a79cf2?q=80&w=2574&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                        ),
                        MyApplication.Companion.Food(
                            "Schweppers",
                            "$3.49",
                            "https://images.unsplash.com/photo-1581006852262-e4307cf6283a?q=80&w=2574&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
                        ),
                        MyApplication.Companion.Food(
                            "Red Bull Energy Drink",
                            "$4.99",
                            "https://images.unsplash.com/photo-1632858280935-d5611683e434?q=80&w=2570&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
                        )
                    )
                )
            )
        ),
        MyApplication.Companion.Restaurant(
            "Restorasa",
            "https://images.unsplash.com/photo-1707066158536-b7daa59a0315?q=80&w=2536&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            listOf(
                MyApplication.Companion.FoodType(
                    "Main Dish",
                    listOf(
                        MyApplication.Companion.Food(
                            "Tomato Pasta",
                            "$10.49",
                            "https://images.unsplash.com/photo-1597131628347-c769fc631754?q=80&w=2670&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                            "Indulge in our authentic Italian-style spaghetti, topped with a savory tomato sauce and garnished with olives and fresh parsley."
                        ),
                        MyApplication.Companion.Food(
                            "Bacon Pasta with Cheese",
                            "$12.49",
                            "https://images.unsplash.com/photo-1579631542720-3a87824fff86?q=80&w=2564&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                            "Our delectable pasta, featuring bacon, mozzarella cheese, and creamy sauce, is a perfect choice for those who love dairy products."
                        ),
                        MyApplication.Companion.Food(
                            "Pepperoni and Olive Pizza",
                            "$10.99",
                            "https://images.unsplash.com/photo-1458642849426-cfb724f15ef7?q=80&w=2000&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                            "Our boss, who originates from Naples, shares his family's traditional Neapolitan pizza with food enthusiasts."
                        ),
                        MyApplication.Companion.Food(
                            "Neapolitan Pizza",
                            "$10.99",
                            "https://images.unsplash.com/photo-1574071318508-1cdbab80d002?q=80&w=2669&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                            "Our boss, who originates from Naples, shares his family's traditional Neapolitan pizza with food enthusiasts."
                        )
                    )
                ),
                MyApplication.Companion.FoodType(
                    "Soup",
                    listOf(
                        MyApplication.Companion.Food(
                            "Pumpkin Soup with Two Waffles",
                            "$6.99",
                            "https://plus.unsplash.com/premium_photo-1679503587085-df01c9ad5b1e?q=80&w=2574&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                            "For those with a sweet tooth like yourself..."
                        ),
                        MyApplication.Companion.Food(
                            "Borscht",
                            "$6.99",
                            "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a7/Borscht_served.jpg/330px-Borscht_served.jpg",
                            "For those who enjoy exploring unique flavors, try our local soup from Eastern Europe."
                        )
                    )
                )
            )
        ),
        MyApplication.Companion.Restaurant(
            "Lotus Garden",
            "https://images.unsplash.com/photo-1668209313677-8cb1ae796de0?q=80&w=2534&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            listOf(
                MyApplication.Companion.FoodType(
                    "Food",
                    listOf(
                        MyApplication.Companion.Food(
                            "Roasted Chicken and Vegetables",
                            "$12.99",
                            "https://images.unsplash.com/photo-1518492104633-130d0cc84637?q=80&w=2526&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                            "Made from handmade dumpling skins and Canadian sourced pork."
                        ),
                        MyApplication.Companion.Food(
                            "Steak and French Fries",
                            "$17.99",
                            "https://images.unsplash.com/photo-1600891964092-4316c288032e?q=80&w=2670&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                            "Made from handmade dumpling skins and Canadian sourced chicken."
                        ),
                    )
                )
            )
        ),
        MyApplication.Companion.Restaurant(
            "Rainbow Bubble Tea",
            "https://images.unsplash.com/photo-1627564803215-ad55bad5c5ea?q=80&w=2574&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            listOf(
                MyApplication.Companion.FoodType(
                    "Bubble Tea",
                    listOf(
                        MyApplication.Companion.Food(
                            "Brown Sugar Milk Bubble Tea",
                            "$7.99",
                            "https://images.unsplash.com/photo-1627564803215-ad55bad5c5ea?q=80&w=2574&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                            "Brown Sugar Milk Bubble Tea is a delicious blend of milk and sweet brown sugar, enhanced with chewy tapioca pearls."
                        ),
                        MyApplication.Companion.Food(
                            "Mocha Milk Bubble Tea",
                            "$7.99",
                            "https://images.unsplash.com/photo-1627564803215-ad55bad5c5ea?q=80&w=2574&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                            "Mocha Milk Bubble Tea combines rich mocha flavor with milk, topped with chewy tapioca pearls."
                        ),
                        MyApplication.Companion.Food(
                            "Bubble Green Tea",
                            "$5.99",
                            "https://images.unsplash.com/photo-1627564803215-ad55bad5c5ea?q=80&w=2574&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8f",
                            "Refreshing green tea infused with chewy tapioca pearls for a delightful twist."
                        ),
                        MyApplication.Companion.Food(
                            "Oreo Milk Bubble Tea",
                            "$7.99",
                            "https://images.unsplash.com/photo-1627564803215-ad55bad5c5ea?q=80&w=2574&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                            "Enjoy creamy milk tea with crunchy Oreo cookies and chewy tapioca pearls. It's a deliciously decadent treat that's perfect for satisfying your sweet cravings with a refreshing twist."
                        ),
                        MyApplication.Companion.Food(
                            "Strawberry Bubble Tea",
                            "$6.49",
                            "https://images.unsplash.com/photo-1627564803215-ad55bad5c5ea?q=80&w=2574&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                            "Savor the sweetness of our Strawberry Bubble Tea, a harmonious blend of juicy strawberries and smooth tea, elevated by the fun chew of tapioca pearls. A refreshing delight in every sip, perfect for bubble tea lovers and newcomers alike."
                        )
                    )
                )
            )
        ),
    )
}*/
