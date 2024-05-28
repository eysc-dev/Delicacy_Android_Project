package com.yschang.delicacy.navhost

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yschang.delicacy.screen.FoodDetailScreen
import com.yschang.delicacy.screen.ForgetPasswordScreen
import com.yschang.delicacy.screen.HistoryScreen
import com.yschang.delicacy.screen.LoginScreen
import com.yschang.delicacy.screen.ProfileDetailScreen
import com.yschang.delicacy.screen.RegisterScreen
import com.yschang.delicacy.screen.home.DetailScreen
import com.yschang.delicacy.screen.home.RestaurantDetailScreen
import com.yschang.delicacy.viewmodel.MainViewModel

//Composable function to define the main navigation host for the app
@Composable
fun MainNavHost( /***** Main Navigation except Bottom *****/
    mainViewModel: MainViewModel = viewModel() //Provides the main view model with default initialization
) {
    val navController = rememberNavController() //Manage navigation state across Composables

    //Collects the state from the main view model to observe changes in login status
    val state by mainViewModel.state.collectAsState()

    //Uses Crossfade to animate between different states based on the login event
    Crossfade(targetState = state.loginEvent, label = "") {

        //If the login state is idle, show a loading indicator
        if (it == MainViewModel.LoginEvent.LOGIN_IDLE) {
            Box(
                modifier = Modifier.fillMaxSize(), //Fill the entire screen
                contentAlignment = Alignment.Center //Center the content
            ) {
                //Jetpack Compose UI (progress indicator)
                /*A progress indicator is a visual element, often rotating or
                featuring a continuous animation, indicating that an operation is currently in progress."*/
                CircularProgressIndicator()
            }
        } else { //Defines the navigation host with different routes and corresponding Composables
            NavHost(
                navController = navController, startDestination =
                if (state.loginEvent == MainViewModel.LoginEvent.LOGIN_SUCCESS) {
                    "home" //Start with home if login is successful
                } else {
                    "login" //Start with login otherwise
                }
            ) {
                composable("login") {
                    LoginScreen(
                        toRegister = {
                            navController.navigate("register") //Navigates to the register screen
                        },
                        toHome = {
                            navController.navigate("home") {

                                //Clears the login route from the back stack
                                popUpTo("login") {
                                    inclusive = true
                                }
                            }
                        },
                        toForget = {
                            //Navigates to the forget password screen
                            navController.navigate("forget")
                        }
                    )
                }
                //Registration route and screen with back navigation
                composable("register") {
                    RegisterScreen(
                        onBack = {
                            //Pops the back stack to return to the previous screen
                            navController.popBackStack()
                        }
                    )
                }
                //Home route and nested navigation host for additional routes
                composable("home") {
                    HomeNavHost(
                        toLogin = {
                            navController.navigate("login") {

                                //Clears the home route on login
                                popUpTo("home") {
                                    inclusive = true
                                }
                            }
                        },
                        /*toDetail = {
                            navController.navigate("detail")
                        },*/
                        toHistory = {
                            //Navigates to the purchase history screen
                            navController.navigate("history")
                        },
                        toRestaurantDetail = {
                            //Navigates to the home (list of restaurants) screen
                            navController.navigate("restaurantDetail")
                        },
                        toProfileDetail = {
                            //Navigates to the profile screen
                            navController.navigate("profileDetail")
                        },
                        toFoodDetail = {
                            //Navigates to the food detail screen
                            navController.navigate("foodDetail")
                        }
                    )
                }
                /*composable("detail") {
                    DetailScreen(
                        onBack = {
                            navController.popBackStack()
                        },
                    )
                }*/
                composable("history") {
                    HistoryScreen(
                        onBack = {
                            navController.popBackStack()
                        }
                    )
                }
                composable("forget") {
                    ForgetPasswordScreen(
                        onBack = {
                            navController.popBackStack() // Navigates back
                        }
                    )
                }
                //home (list of restaurants) screen
                composable("restaurantDetail") {
                    RestaurantDetailScreen(
                        onBack = {
                            navController.popBackStack() // Navigates back
                        },
                        toDetail = {
                            navController.navigate("foodDetail")
                        }
                    )
                }
                composable("profileDetail") {
                    ProfileDetailScreen(
                        onBack = {
                            navController.popBackStack() // Navigates back
                        }
                    )
                }
                composable("foodDetail") {
                    FoodDetailScreen(
                        onBack = {
                            navController.popBackStack() // Navigates back
                        }
                    )
                }
            }
        }
    }
}