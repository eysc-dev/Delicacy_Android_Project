package com.yschang.delicacy.navhost

import androidx.compose.foundation.layout.WindowInsets  //For UI spacing
import androidx.compose.foundation.layout.padding  //For setting padding in the UI
import androidx.compose.material.icons.Icons  //Material icons
import androidx.compose.material.icons.outlined.Home  //Outlined icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.twotone.Favorite  //Two-tone icons
import androidx.compose.material.icons.twotone.FavoriteBorder
import androidx.compose.material.icons.twotone.Home
import androidx.compose.material.icons.twotone.Person
import androidx.compose.material.icons.twotone.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector  //For handling vector graphics
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.yschang.delicacy.screen.home.CartScreen
import com.yschang.delicacy.screen.home.FavoriteScreen
//import com.yschang.delicacy.screen.home.HomeScreen
import com.yschang.delicacy.screen.home.ProfileScreen
import com.yschang.delicacy.screen.home.RestaurantScreen

@Composable
fun HomeNavHost( /***** Bottom Navigation *****/
    toLogin: () -> Unit, //Navigation callback for the login screen
    //toDetail: () -> Unit, //A home screen ( list of restaurants screen)
    toHistory: () -> Unit, //A purchase history screen that I didn't use
    toRestaurantDetail: () -> Unit, //A home screen ( list of restaurants screen)
    toProfileDetail: () -> Unit, //A profile detail screen
    toFoodDetail:()->Unit //Navigation callback for a food detail screen
) {
    val navController = rememberNavController() //Create a navigation controller

    //Get the current back stack entry
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination //Get the current location

    //List of bottom navigation screens
    val list = listOf(
        Screen.Rest, //Resturant
        Screen.Favorite,
        Screen.Cart, //Shopping Cart
        Screen.Profile //User Profile
    )

    //Scaffold UI layout component in Jetpack Compose (eg. TopBar)
    Scaffold( //Layout with a bottom navigation bar.
        bottomBar = {
            NavigationBar {
                list.forEach { screen -> //Loop through each screen
                    NavigationBarItem(
                        icon = {
                            //Determine which icon to display based on the current destination
                            Icon(imageVector =
                            if (currentDestination?.hierarchy?.any { it.route == screen.route } == true) {
                                screen.selected //Selected icon
                            } else {
                                screen.unSelected //Unselected icon
                            }, contentDescription = null)

                        },
                        label = {
                            //Label for the navigation item
                            Text(text = screen.label)
                        },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            //Handle navigation when an item is clicked
                            navController.navigate(screen.route) {
                                // Ensure correct back stack behavior
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true //Save the current state
                                }
                                launchSingleTop = true //Prevent duplicate destinations
                                restoreState = true //Restore saved state
                            }
                        }
                    )
                }
            }
        },
        /*The Scaffold component provides a default window inset area to
        ensure content does not overlap with system elements. Setting the window inset area to zero
        brings the content area to the window edge, disregarding the default insets.*/
        contentWindowInsets = WindowInsets(0) //Disables window insets
    ) {
        //Define the navigation host with a starting destination
        NavHost(
            navController = navController,
            startDestination = "rest", // The starting screen (restaurant)
            modifier = Modifier.padding(it)
        ) {
            /*composable("home") {
                HomeScreen(
                    toDetail = toDetail //Pass the detail navigation callback
                )
            }*/
            //Shopping cart screen
            composable("cart") {
                CartScreen(

                )
            }
            composable("profile") {
                ProfileScreen(
                    toLogin = toLogin, //Pass the login navigation callback
                    toHistory = toHistory, //Pass the history navigation callback
                    toProfileDetail = toProfileDetail //Pass the profile detail navigation callback
                )
            }
            //Home screen = List of restaurants screen
            composable("rest") {
                RestaurantScreen(
                    toRestaurantDetail = toRestaurantDetail
                )
            }
            composable("favorite") {
                FavoriteScreen(
                    toDetail = toFoodDetail
                )
            }
        }
    }

}

sealed class Screen( //Define a sealed class for screen representation
    val route: String, //The navigation route
    val label: String, //The label for the navigation item
    val selected: ImageVector, //The icon when the item is selected
    val unSelected: ImageVector //The icon when the item is not selected
) {
    //data object Home : Screen("home", "Home", Icons.TwoTone.Home, Icons.Outlined.Home)
    data object Rest : //Home screen (List of Restaurants screen)
        Screen("rest", "Home", Icons.TwoTone.Home, Icons.Outlined.Home)
    data object Favorite : // Favorite screen
        Screen("favorite", "Favorite", Icons.TwoTone.Favorite, Icons.TwoTone.FavoriteBorder)
    data object Cart : // Shopping cart screen
        Screen("cart", "Cart", Icons.TwoTone.ShoppingCart, Icons.Outlined.ShoppingCart)
    data object Profile : // User profile screen
        Screen("profile", "Profile", Icons.TwoTone.Person, Icons.Outlined.Person)
}