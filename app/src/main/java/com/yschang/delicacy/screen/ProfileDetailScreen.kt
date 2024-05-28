package com.yschang.delicacy.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.twotone.ArrowBack
import androidx.compose.material.icons.twotone.Edit
import androidx.compose.material.icons.twotone.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yschang.delicacy.viewmodel.ProfileDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileDetailScreen( /***** User Profile Screen (小框框裡面的) *****/
    profileDetailViewModel: ProfileDetailViewModel = viewModel(),
    onBack: () -> Unit //Back navigation
) {
    val state by profileDetailViewModel.state.collectAsState() //Collect the state from the ViewModel
    var editDialogShow by remember {
        mutableStateOf(false) // State to show/hide the edit dialog
    }
    var email by remember { mutableStateOf(state.email) }
    var fullName by remember { mutableStateOf(state.fullName) }
    var phoneNumber by remember { mutableStateOf(state.phoneNumber) }

    if (editDialogShow) { //If the edit dialog should be shown
        AlertDialog( //Create the edit dialog
            onDismissRequest = { editDialogShow = false }, //Action to dismiss the dialog
            title = { Text(text = "Edit Profile") },
            text = {
            Column( //Column layout for the dialog content
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Surface( //The circular user icon
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(16.dp),
                    shape = CircleShape
                ) {
                    Icon( //The person icon for the profile
                        imageVector = Icons.TwoTone.Person, contentDescription = null,
                        modifier = Modifier
                            .size(64.dp)
                            .padding(8.dp)
                    )
                }
                Text(
                    text = state.username,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.size(16.dp)) //Add a spacer

                OutlinedTextField(
                    value = email, //Current email value
                    onValueChange = { email = it }, //Update the email
                    label = { Text(text = "Email") }) //Label for the text field

                Spacer(modifier = Modifier.size(16.dp))

                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = { Text(text = "Full Name") })

                Spacer(modifier = Modifier.size(16.dp))

                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    label = { Text(text = "Phone Number") })
            }
        }, confirmButton = {
            TextButton(onClick = { //Action to confirm changes
                profileDetailViewModel.editProfile(email, fullName, phoneNumber)
                editDialogShow = false
            }) {
                Text(text = "Save")
            }
        }, dismissButton = {
            TextButton(onClick = { //Action to dismiss the dialog
                editDialogShow = false
            }) {
                Text(text = "Cancel")
            }
        })
    }

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(text = "User Profile") //Set the title to "User Profile"
            }, navigationIcon = {
                IconButton(onClick = {
                    onBack()
                }) {
                    Icon( // The back arrow icon
                        imageVector = Icons.AutoMirrored.TwoTone.ArrowBack,
                        contentDescription = null
                    )
                }
            })
        },

        floatingActionButton = { //The floating action button for editing
            LargeFloatingActionButton(onClick = { editDialogShow = true })  //Show the edit dialog
            {
                Icon(imageVector = Icons.TwoTone.Edit, contentDescription = null)
            }
        },

        contentWindowInsets = WindowInsets(0),

        floatingActionButtonPosition = FabPosition.EndOverlay //Position for the floating action button
    ) {
        //Box to contain the main content
        Box(modifier = Modifier.padding(it)) {
            AnimatedContent(targetState = state.isLoading, label = "") {
                if (it) { //If the content is loading
                    CircularProgressIndicator( // Displays a loading indicator
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else { //If the content is not loading, show the current profile details
                    OutlinedCard(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        onClick = {
                        }
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Surface(
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(16.dp),
                                shape = CircleShape
                            ) {
                                Icon(
                                    imageVector = Icons.TwoTone.Person, contentDescription = null,
                                    modifier = Modifier
                                        .size(64.dp)
                                        .padding(8.dp)
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Username: ",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = state.username,
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                            Spacer(modifier = Modifier.size(16.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Email: ",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = state.email,
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                            Spacer(modifier = Modifier.size(16.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Full Name: ",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = state.fullName,
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                            Spacer(modifier = Modifier.size(16.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Phone Number: ",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = state.phoneNumber,
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}