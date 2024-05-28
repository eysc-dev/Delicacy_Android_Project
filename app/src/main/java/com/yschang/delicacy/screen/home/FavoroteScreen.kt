package com.yschang.delicacy.screen.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Favorite
import androidx.compose.material.icons.twotone.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.yschang.delicacy.MyApplication
import com.yschang.delicacy.viewmodel.home.FavoriteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteScreen( /***** Favorite Screen *****/
    favoriteViewModel: FavoriteViewModel = viewModel(),
                    toDetail:()->Unit //Navigate to the detail screen
) {
    val state by favoriteViewModel.state.collectAsState() //Collect the state for this ViewModel
    var isShowDialog by remember {
        mutableStateOf(false) //State for whether the dialog should be shown
    }
    if (isShowDialog) { //If the confirmation dialog should be displayed
        AlertDialog( //Create the confirmation dialog
            onDismissRequest = {
            isShowDialog = false
        }, confirmButton = {
            TextButton(onClick = {
                favoriteViewModel.unFavorite( //Remove the favorite item
                    MyApplication.currentFood
                )
                isShowDialog = false //Hide the dialog without taking action
            }) {
                Text(text = "OK")
            }
        }, title = {
            Text(text = "Do you want to delete favorite food?")
        }, text = {
            Text(text = "This action cannot be undone.")
        }, dismissButton = {
            TextButton(onClick = {
                isShowDialog = false
            }) {
                Text(text = "Cancel")
            }
        })
    }
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(text = "Favorite")
            }, actions = {
                IconButton(onClick = {
                    favoriteViewModel.refresh() //Refresh button action
                }) {
                    //The refresh icon
                    Icon(imageVector = Icons.TwoTone.Refresh, contentDescription = null)
                }
            })
        }, contentWindowInsets = WindowInsets(0)
    ) { paddingValues ->
        AnimatedContent( //Animated transition for content changes
            state.isLoading, label = "", //Set the state to watch and a label for tracking
            modifier = Modifier.padding(paddingValues)
        ) { it ->
            if (it) { //If the content is loading
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator() //Display a circular loading indicator
                }
            } else {
                if (state.favoriteList.isEmpty()) { //If there are no favorite items
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "No favorite food") //Display the "No favorite food" message
                    }
                } else {
                    LazyColumn( //A lazy-loaded column for the list of favorite items
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(
                            state.favoriteList
                        ) {
                            OutlinedCard(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                onClick = {
                                    MyApplication.currentFood = it
                                    toDetail()
                                }
                            ) {
                                Row(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                                    ) {
                                        AsyncImage(
                                            model = ImageRequest.Builder(LocalContext.current)
                                                .data(it.image)
                                                .crossfade(true)
                                                .build(),
                                            contentDescription = null,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(16.dp))
                                                .size(64.dp)
                                        )
                                        Text(text = it.name) //Display food name
                                    }
                                    IconButton(onClick = { //Action for the favorite button
                                        MyApplication.currentFood = it
                                        isShowDialog = true //Show the delete confirmation dialog
                                    }) {
                                        Icon(
                                            imageVector = Icons.TwoTone.Favorite,
                                            contentDescription = null
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}