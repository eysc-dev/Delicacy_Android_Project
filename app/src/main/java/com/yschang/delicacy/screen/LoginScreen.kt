package com.yschang.delicacy.screen

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yschang.delicacy.viewmodel.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen( /***** Login History Screen *****/
    loginViewModel: LoginViewModel = viewModel(),
    toRegister: () -> Unit, //Navigate to the registration screen
    toHome: () -> Unit, //Navigate to the home screen
    toForget:()->Unit //Navigate to the "forget password" screen
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val state by loginViewModel.state.collectAsState() //Collect the state from the ViewModel

    Scaffold( //Scaffold structure for the screen
        topBar = {
            TopAppBar(title = {
                Text(text = "Login")
            })
        }, contentWindowInsets = WindowInsets(0)
    ) { paddingValues -> //The content area of the Scaffold
        Column(
            Modifier
                .padding(paddingValues) //Applies padding from the Scaffold
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedCard( //Outlined card for the login form
                modifier = Modifier
                    .padding(16.dp)
                    .imePadding(), //Adjust for the keyboard input area
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
                ) {
                    Crossfade(targetState = state.loginEvent, label = "") {
                        if (it == LoginViewModel.LoginEvent.LOGIN_FAILED) { //If login failed
                            Text( //Display a failure message
                                text = "Login failed",
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.error
                            )
                        } else if (it == LoginViewModel.LoginEvent.LOGIN_SUCCESS) { //If login succeeded
                            Text( //Display a success message
                                text = "Login success",
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    OutlinedTextField( //Text field for the username
                        value = username, //The current username value
                        onValueChange = { username = it }, //Update the username
                        label = { Text(text = "Username") },
                        modifier = Modifier.width(TextFieldDefaults.MinWidth)
                    )
                    OutlinedTextField( //Text field for the password
                        value = password, //The current password value
                        onValueChange = { password = it }, //Updates the password
                        label = { Text(text = "Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.width(TextFieldDefaults.MinWidth)
                    )
                    Button(onClick = { //Button to submit the login form
                        loginViewModel.login(
                            username,
                            password,
                            toHome // Navigates to the home screen if successful
                        )
                    }) {
                        Text(text = "Login") //Text for the login button
                    }
                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ClickableText( //Clickable text for navigating to registration
                            text = buildAnnotatedString {//Build the annotated text
                            withStyle(
                                SpanStyle(
                                    color = MaterialTheme.colorScheme.primary
                                )
                            ) {
                                append("Don't have an account yet?")
                            }
                        }) {
                            toRegister() //Navigate to the registration screen
                        }
                        ClickableText(text = buildAnnotatedString {
                            withStyle(
                                SpanStyle(
                                    color = MaterialTheme.colorScheme.error
                                )
                            ) {
                                append("Forgot password?")
                            }
                        }) {
                            toForget() //Navigates to the forget password screen
                        }
                        Text( // Accessibility
                            text = "Need any help? Please contact us via " +
                                    "customer@delicacy.com or 204-168-1168",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}