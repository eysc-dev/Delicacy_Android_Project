package com.yschang.delicacy.screen.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.yschang.delicacy.MyApplication

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantScreen( /***** List of Restaurants = Home Screen *****/
    toRestaurantDetail:() -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(text = "Restaurant")
            })
        }
    ) {
        LazyColumn(Modifier.padding(it)) {
            items(MyApplication.restaurants) {
                OutlinedCard(
                    modifier = Modifier.padding(16.dp)
                ) {
                    ListItem(headlineContent = {
                        Text(text = it.name) //Display the restaurant name
                    }, leadingContent = {
                        AsyncImage( //Asynchronously loads and displays the restaurant image
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(it.image) //url
                                .crossfade(true)
                                .build(),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .size(64.dp)
                        )
                    }, modifier = Modifier.clickable { //Make the list item clickable
                        MyApplication.currentRestaurant = it //Set the current restaurant
                        toRestaurantDetail() // Navigates to the restaurant detail screen (eg. Main Dish)
                    })
                }
            }
        }
    }
}