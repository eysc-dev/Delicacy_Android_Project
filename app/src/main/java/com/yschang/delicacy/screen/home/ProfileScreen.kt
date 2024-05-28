package com.yschang.delicacy.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.twotone.Logout
import androidx.compose.material.icons.twotone.History
import androidx.compose.material.icons.twotone.Person
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yschang.delicacy.viewmodel.home.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen( /***** Profile Screen (小框框裡面的) *****/
    profileViewModel: ProfileViewModel = viewModel(),
    toLogin: () -> Unit, //navigate to the login screen
    toHistory: () -> Unit, //navigate to the purchase history that I didn't use
    toProfileDetail:()->Unit //User Profile Screen (小框框)
) {
    val state by profileViewModel.state.collectAsState() // Collect the state from the ViewModel
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "Profile") })
        }, contentWindowInsets = WindowInsets(0)
    ) {
        Column(
            Modifier.padding(it)
        ) {
            OutlinedCard(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                onClick = {
                    toProfileDetail()
                }
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Surface( //the user caricular icon
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
                    Text( // Displays the username
                        text = state.username,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.size(16.dp))
                    Text(text = state.email) // Displays the email address
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            /*OutlinedCard(modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
                onClick = {
                    toHistory()
                }) {
                Row(
                    Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.TwoTone.History,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(text = "Purchase History")
                }
            }*/
            OutlinedCard(modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
                onClick = {
                    profileViewModel.logout(
                        toLogin
                    )
                }) {
                Row(
                    Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.TwoTone.Logout,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(text = "Logout")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}