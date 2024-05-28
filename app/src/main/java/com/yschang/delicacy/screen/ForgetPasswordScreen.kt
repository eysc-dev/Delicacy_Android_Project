package com.yschang.delicacy.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.twotone.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yschang.delicacy.viewmodel.ForgetPasswordViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgetPasswordScreen( /***** Forget Password Screen *****/
    onBack: () -> Unit,
    forgetPasswordViewModel: ForgetPasswordViewModel = viewModel()
) {
    val state by forgetPasswordViewModel.state.collectAsState() //Collect the state from the ViewModel
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var repeatPassword by remember { mutableStateOf("") }
    var code by remember { mutableStateOf("") } //Code is user's email verification code

    Scaffold( //Scaffold (UI layout) structure for the screen
        topBar = {
            TopAppBar(title = {
                Text(text = "Forget Password")
            }, navigationIcon = {
                IconButton(onClick = {
                    onBack()
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.TwoTone.ArrowBack,
                        contentDescription = null
                    )
                }
            })
        }
    ) { paddingValues ->
        Column(
            Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedCard(
                modifier = Modifier.padding(16.dp)
            ) {
                Column(
                    Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    //Animated content for displaying messages
                    AnimatedContent(
                        targetState = state.forgetPasswordEvent, //Observe the current event state
                        label = "")
                    {
                        when (it) { //Handle different event states (success or no)
                            is ForgetPasswordViewModel.ForgetPasswordEvent.FORGET_PASSWORD_SUCCESS -> {
                                //Display success message
                                Text(it.reason, color = MaterialTheme.colorScheme.primary)
                            }

                            is ForgetPasswordViewModel.ForgetPasswordEvent.FORGET_PASSWORD_FAILED -> {
                                //Display failure message
                                Text(it.reason, color = MaterialTheme.colorScheme.error)
                            }

                            ForgetPasswordViewModel.ForgetPasswordEvent.FORGET_PASSWORD_IDLE -> {

                            }
                        }
                    }
                    OutlinedTextField(
                        value = username, //The current username
                        onValueChange = { username = it },
                        label = { Text("Username") }, //Label for the text field
                        modifier = Modifier.width(TextFieldDefaults.MinWidth))
                    OutlinedTextField(
                        value = password, //The current password
                        onValueChange = { password = it }, //Update the password
                        label = { Text("New Password") }, //Label for the text field
                        modifier = Modifier.width(TextFieldDefaults.MinWidth))
                    OutlinedTextField(
                        value = repeatPassword,
                        onValueChange = { repeatPassword = it },
                        label = { Text("Repeat Password") },
                        modifier = Modifier.width(TextFieldDefaults.MinWidth))

                    Row( //Row layout for the code and send button
                        modifier = Modifier.width(TextFieldDefaults.MinWidth),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = code, //The current code value
                            onValueChange = { code = it }, //Update the code
                            label = { Text("Code") },
                            modifier = Modifier.weight(1f)) //Give flexible space
                        Spacer(modifier = Modifier.width(16.dp))
                        FilledTonalButton(onClick = { // Button to send the code
                            forgetPasswordViewModel.send(username) //Action to send the code
                        }) {
                            Text("Send Code")
                        }
                    }
                    Button(onClick = { //Button to submit the form
                        forgetPasswordViewModel.forgetPassword( //Call the ViewModel method
                            username,
                            password, //New Pwd
                            repeatPassword, //New Pwd
                            code
                        )
                    }) {
                        Text("Submit")
                    }
                }
            }
        }
    }
}