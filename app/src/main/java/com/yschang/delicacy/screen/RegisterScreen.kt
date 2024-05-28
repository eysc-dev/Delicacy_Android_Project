package com.yschang.delicacy.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.twotone.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yschang.delicacy.viewmodel.RegisterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen( /***** Register Screen *****/
    registerViewModel: RegisterViewModel = viewModel(),
    onBack: () -> Unit //Back navigation (to Login screen)
                    //LoginScreen.kt 中的 "Don't have an account yet?" 有 toRegister()
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var code by remember { mutableStateOf("") } //Email verification code
    var fullName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    val state by registerViewModel.state.collectAsState() //Collect the state from the ViewModel

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "Register") },
                navigationIcon = {
                IconButton(onClick = {
                    onBack()
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.TwoTone.ArrowBack,
                        contentDescription = null
                    )
                }
            })
        }, contentWindowInsets = WindowInsets(0)
    ) { paddingValues -> //The content area of the Scaffold
        Column(
            Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedCard(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
                ) {
                    //Animated content transition for registration events
                    AnimatedContent(targetState = state.registerEvent, label = "") {
                        when (it) {
                            // If registration was successful
                            is RegisterViewModel.RegisterEvent.REGISTER_SUCCESS -> {
                                // Displays a success message
                                Text(it.reason, color = MaterialTheme.colorScheme.primary)
                            }
                            // If registration failed
                            is RegisterViewModel.RegisterEvent.REGISTER_FAILED -> {
                                // Displays a failure message
                                Text(it.reason, color = MaterialTheme.colorScheme.error)
                            }
                            RegisterViewModel.RegisterEvent.REGISTER_IDLE -> {
                            }
                        }
                    }
                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text(text = "Username") },
                        modifier = Modifier.width(TextFieldDefaults.MinWidth),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text(text = "Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.width(TextFieldDefaults.MinWidth),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text(text = "Email") },
                        modifier = Modifier.width(TextFieldDefaults.MinWidth),
                        singleLine = true
                    )
                    OutlinedTextField(value = fullName, onValueChange = { fullName = it },
                        label = { Text(text = "FullName") },
                        modifier = Modifier.width(TextFieldDefaults.MinWidth),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = phoneNumber,
                        onValueChange = { phoneNumber = it },
                        label = { Text(text = "PhoneNumber") },
                        modifier = Modifier.width(TextFieldDefaults.MinWidth),
                        singleLine = true
                    )
                    Row( // Row layout for the verification code and send button
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .width(TextFieldDefaults.MinWidth)
                    ) {
                        OutlinedTextField(value = code, onValueChange = { code = it },
                            label = { Text(text = "Code") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        ElevatedButton(onClick = {
                            registerViewModel.send(email)
                        }) { Text(text = "Send") }
                    }
                    // Button to submit the registration form
                    Button(onClick = {
                        registerViewModel.register( // Calls the ViewModel method to register
                            username,
                            password,
                            email,
                            code,
                            fullName,
                            phoneNumber
                        )
                    }) {
                        Text(text = "Register")
                    }
                }
            }
        }
    }
}