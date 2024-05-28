package com.yschang.delicacy.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.twotone.ArrowBack
import androidx.compose.material.icons.twotone.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
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
import com.yschang.delicacy.viewmodel.HistoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen( /***** Purchase History Screen that I didn't Use *****/
    onBack: () -> Unit, //Handle back navigation
    historyViewModel: HistoryViewModel = viewModel()
) {
    val state by historyViewModel.state.collectAsState() //Collect the state from the ViewModel

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(text = "Purchase History")
            }, navigationIcon = { //Navigation icon for back navigation
                IconButton(onClick = { onBack() })  //Back button action
                {
                    Icon( //The back arrow "<-" icon
                        imageVector = Icons.AutoMirrored.TwoTone.ArrowBack,
                        contentDescription = null
                    )
                }
            }, actions = {
                IconButton(onClick = { historyViewModel.refresh() })  //Refresh button action
                {
                    Icon( //The refresh icon
                        imageVector = Icons.TwoTone.Refresh,
                        contentDescription = null
                    )
                }

            })
        }, contentWindowInsets = WindowInsets(0)
    ) { paddingValues -> //The content area of the Scaffold
        AnimatedContent( //Animated transition for the content
            targetState = state.cart, label = "", //Observe the state of the cart
            modifier = Modifier
                .padding(paddingValues) //Apply padding from the Scaffold
                .fillMaxSize()
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                if (it.isEmpty()) { //If the cart is empty
                    Text(
                        text = "Empty History", //Display the "Empty History" message
                        style = MaterialTheme.typography.titleLarge
                    )
                } else { //If there are items in the cart
                    LazyColumn( //Lazy-loaded column for displaying the cart items
                        modifier = Modifier.fillMaxSize()
                    ) {
                        itemsIndexed(state.cart) { _, food -> //Loop through the cart items
                            OutlinedCard(modifier = Modifier.padding(8.dp))
                            {
                                ListItem( //Define the layout for each list item
                                    headlineContent = {
                                    Text(text = food.name) //Display the name of food
                                }, leadingContent = {
                                    AsyncImage( //Load and display the item image asynchronously
                                        model = ImageRequest.Builder(LocalContext.current)
                                            .data(food.image) //Url
                                            .crossfade(true) //Enable crossfade animation
                                            .build(),
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop, //Scale the image by cropping
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(16.dp))
                                            .size(64.dp)
                                    )
                                },
                                    supportingContent = {
                                        Text(text = food.price) //Display the price of food
                                    },
                                    trailingContent = {
                                        AnimatedContent( //Animated content for the quantity
                                            targetState = food.number.toString(),
                                            label = "" //Label for the animation
                                        ) {
                                            Text( //Display the quantity
                                                text = it,
                                                style = MaterialTheme.typography.titleLarge
                                            )

                                        }
                                    })
                            }
                        }
                    }
                }
            }
        }
    }
}