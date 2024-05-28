/***** Home Screen is Restaurant*****/

/*
package com.yschang.delicacy.screen.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.yschang.delicacy.MyApplication
import com.yschang.delicacy.viewmodel.home.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = viewModel(),
    toDetail: () -> Unit //Function to navigate to the Detail screen
) {
    Scaffold( //Scaffold UI layout component in Jetpack Compose
        topBar = {
            TopAppBar(title = {
                Text(text = "Home") //Set the title to "Home"
            })
        }, contentWindowInsets = WindowInsets(0)
    ) {
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            items(MyApplication.foods) {
                ListItem(headlineContent = {
                    Text(text = it.name)
                }, leadingContent = {
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
                }, trailingContent = {
                    Text(text = it.price)
                }, modifier = Modifier.clickable {
                    MyApplication.food = it
                    toDetail()
                })
            }
        }
    }
}*/
