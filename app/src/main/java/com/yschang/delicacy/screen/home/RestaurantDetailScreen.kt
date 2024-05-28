package com.yschang.delicacy.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.twotone.ArrowBack
import androidx.compose.material.icons.automirrored.twotone.Sort
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.yschang.delicacy.viewmodel.RestaurantDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantDetailScreen( /***** List of Dishes in the Restaurant Screen *****/
    onBack: () -> Unit,
    toDetail: () -> Unit,
    restaurantDetailViewModel: RestaurantDetailViewModel = viewModel()
) {
    val state by restaurantDetailViewModel.state.collectAsState() //Collect state from the ViewModel

    Scaffold( //Scaffold UI layout component in Jetpack Compose (eg. TopBar)
        topBar = {
            TopAppBar(title = {
                //Set the CURRENT restaurant name as the title
                Text(text = MyApplication.currentRestaurant.name)
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
    ) {
        LazyColumn(
            Modifier.padding(it)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text( //Display the type of food (eg. Main Dish, Soup)
                        text = MyApplication.currentRestaurant.foodTypes[0].type, //[0] gets the first element
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    //Button to sort the food list (Price from Low to High, Price from High to Low)
                    FilledIconButton(onClick = {
                        restaurantDetailViewModel.sortFood()
                    }) {
                        Icon( //The sort icon
                            imageVector = Icons.AutoMirrored.TwoTone.Sort,
                            contentDescription = null
                        )
                    }
                }
            }
            items(state.foodList) {//Food List
                OutlinedCard(
                    modifier = Modifier.padding(16.dp),
                    onClick = {
                        MyApplication.currentFood = it //Set the current food item in the app
                        toDetail() //Go to Food Detail Screen
                    }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.padding(16.dp)
                    ) {
                        AsyncImage( //Asynchronously loads and displays the food image
                            model = ImageRequest.Builder(LocalContext.current) //Build the image request
                                .data(it.image) //Set the url for the image
                                .crossfade(true) //Enable crossfade animation
                                .build(),
                            contentDescription = null,
                            contentScale = ContentScale.Crop, //Scale the image by cropping
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .size(64.dp)
                        )
                        Text( //Display the food name
                            text = it.name,
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier.weight(1f) //Give the text flexible space
                        )
                        Text(text = it.price) //Display the food price
                    }
                }
            }
            if (state.drinkList.isNotEmpty()) { //If there are drinks to display
                item {
                    Row(
                        Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text( //Display the drink type
                            text = MyApplication.currentRestaurant.foodTypes[1].type,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary, //Primary color for the text
                        )
                        FilledIconButton(onClick = { //Button to sort the drink list
                            restaurantDetailViewModel.sortDrink() //Action to sort drinks
                        }) {
                            Icon( //The sort icon
                                imageVector = Icons.AutoMirrored.TwoTone.Sort,
                                contentDescription = null
                            )
                        }
                    }
                }
                items(state.drinkList) { //Drinks list
                    OutlinedCard(
                        modifier = Modifier.padding(16.dp),
                        onClick = {
                            MyApplication.currentFood = it //Set the current food item in the app
                            toDetail() //Go to Food Detail Screen
                        }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.padding(16.dp)
                        ) {
                            AsyncImage( //Asynchronously loads and displays the drink image
                                model = ImageRequest.Builder(LocalContext.current) //Build the image request
                                    .data(it.image) //url
                                    .crossfade(true)
                                    .build(),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .size(64.dp)
                            )
                            Text(
                                text = it.name,
                                style = MaterialTheme.typography.titleSmall,
                                modifier = Modifier.weight(
                                    1f
                                )
                            )
                            Text(text = it.price) //Display the drink price
                        }
                    }
                }
            }
            if (state.otherList.isNotEmpty()) { //If there are other type to display
                item { //An item in the LazyColumn for other items
                    Row(
                        Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text( //Display the type for other items
                            text = MyApplication.currentRestaurant.foodTypes[2].type,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary,
                        )
                        FilledIconButton(onClick = { //Button to sort other items
                            restaurantDetailViewModel.sortOther()
                        }) {
                            Icon( //The sort icon
                                imageVector = Icons.AutoMirrored.TwoTone.Sort,
                                contentDescription = null
                            )
                        }
                    }
                }
                items(state.otherList) { //List of other type
                    OutlinedCard(
                        modifier = Modifier.padding(16.dp),
                        onClick = {
                            MyApplication.currentFood = it
                            toDetail() //Go to Food Detail Screen
                        }
                    ) {
                        Row( //Row layout for the card content (other type)
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.padding(16.dp)
                        ) {
                            AsyncImage(  //Asynchronously loads and displays the other item image
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(it.image) //.data(otherItem.image)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .size(64.dp)
                            )
                            Text( //Display the name of the other type
                                text = it.name,
                                style = MaterialTheme.typography.titleSmall,
                                modifier = Modifier.weight(
                                    1f
                                )
                            )
                            Text(text = it.price)
                        }
                    }
                }
            }
        }
    }
}