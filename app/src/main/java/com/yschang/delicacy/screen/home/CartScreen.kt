package com.yschang.delicacy.screen.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Add
import androidx.compose.material.icons.twotone.Refresh
import androidx.compose.material.icons.twotone.Remove
import androidx.compose.material.icons.twotone.ShoppingCartCheckout
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.yschang.delicacy.viewmodel.SharedState
import com.yschang.delicacy.viewmodel.home.CartViewModel

//This Composable defines a screen for displaying items in a shopping cart
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CartScreen(cartViewModel: CartViewModel = viewModel()) {
    val state by SharedState.state.collectAsState() //Collects the shared state as Compose state
    var isDeleteDialogShow by remember {
        mutableStateOf(false) //Manages the state for the delete confirmation dialog
    }
    var index0 by remember {
        mutableIntStateOf(0) //Holds the index of the item to delete
    }

    //If the delete confirmation dialog should be displayed
    if (isDeleteDialogShow) {
        AlertDialog(onDismissRequest = {
            isDeleteDialogShow = false  //Closes the dialog when dismissed
        }, title = {
            Text(text = "Delete") //Dialog title
        }, text = {
            Text(text = "Are you sure you want to delete?") // Dialog message
        }, confirmButton = {
            Button(onClick = {
                cartViewModel.delete(index0) //Deletes the item at the specified index
                isDeleteDialogShow = false //Hides the dialog
            }) {
                Text(text = "Yes") //Confirm button text
            }
        }, dismissButton = {
            TextButton(onClick = {
                isDeleteDialogShow = false //Hides the dialog without deleting
            }) {
                Text(text = "No") //Dismiss button text
            }
        })
    }
    //Scaffold UI for the main layout with a top bar and content area.
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Column {
                    Text(text = "Cart") //Title of the screen
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Long press to delete items", //Subtitle with instructions
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }, actions = {
                IconButton(onClick = {
                    cartViewModel.refresh() //Refreshes the shopping cart
                }) {
                    //The icon for the refresh button
                    Icon(imageVector = Icons.TwoTone.Refresh, contentDescription = null)
                }
            })
        },
        contentWindowInsets = WindowInsets(0), //Disables window insets
    ) { paddingValues ->
        Box( //The container for the content with a central alignment
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            //Crossfade animation based on loading state
            Crossfade(targetState = state.isLoading, label = "") { isLoading ->
                if (isLoading) {
                    CircularProgressIndicator() //Progress indicator (A visual element)
                } else {
                    Crossfade(targetState = state.cart.isEmpty(), label = "") { it ->
                        if (it) { //If the shopping cart is empty
                            Text(
                                text = "Empty Cart", //Display "Empty Cart" in the Cart screen
                                style = MaterialTheme.typography.titleLarge //The text style
                            )
                        } else { //If the shopping cart is NOT empty
                            Column( //The main layout for displaying cart items
                                modifier = Modifier.fillMaxSize()
                            ) {
                                //Lazy-loaded list for cart items
                                LazyColumn(modifier = Modifier.weight(1f)) {
                                    //Loops through the cart items
                                    itemsIndexed(state.cart) { index, food ->
                                        //A card with an outline for each cart item
                                        OutlinedCard(
                                            modifier = Modifier.padding(8.dp)
                                        ) {
                                            //Defines the layout and content for each cart item
                                            ListItem(headlineContent = {
                                                Text(text = food.name) //The main title for the item
                                            }, leadingContent = {
                                                AsyncImage(
                                                    //The image request for loading
                                                    model = ImageRequest.Builder(LocalContext.current)
                                                        .data(food.image) //The source of the image
                                                        .crossfade(true) //Enables crossfade animation
                                                        .build(),
                                                    contentDescription = null,
                                                    //Scales the image by cropping
                                                    contentScale = ContentScale.Crop,
                                                    modifier = Modifier
                                                        //Clips the image with rounded corners
                                                        .clip(RoundedCornerShape(16.dp))
                                                        .size(64.dp)
                                                )
                                            },
                                                supportingContent = {
                                                    Text(text = food.price)

                                                },
                                                trailingContent = {
                                                    //Row layout for the controls
                                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                                        IconButton(onClick = {
                                                            //Decrease the item quantity
                                                            cartViewModel.minus(index)
                                                        }) {
                                                            //The minus "-" button icon
                                                            Icon(
                                                                //"-" icon from Material Icons
                                                                imageVector = Icons.TwoTone.Remove,
                                                                contentDescription = null
                                                            )
                                                        }
                                                        //Need to import androidx.compose.animation.AnimatedContent
                                                        //Utilize the default animation effect (crossfade) provided by Jetpack Compose.
                                                        AnimatedContent( //Animate the item quantity
                                                            targetState = food.number.toString(),
                                                            label = ""
                                                        ) {
                                                            Text(
                                                                text = it, //The item quantity
                                                                style = MaterialTheme.typography.titleLarge
                                                            )

                                                        }
                                                        IconButton(onClick = {
                                                            //Increase the item quantity
                                                            cartViewModel.plus(index)
                                                        }) {
                                                            //The plus "+" button icon
                                                            Icon(
                                                                //"+" icon from Material Icons
                                                                imageVector = Icons.TwoTone.Add,
                                                                contentDescription = null
                                                            )
                                                        }
                                                    }
                                                },
                                                //Defines combined click and long-click behavior
                                                modifier = Modifier.combinedClickable(
                                                    //Long-press action for deleting
                                                    onLongClick = {
                                                        //Mark the item to be deleted
                                                        index0 = index
                                                        //Show the delete confirmation dialog
                                                        isDeleteDialogShow = true
                                                    },
                                                    onClick = {

                                                    }
                                                ),
                                                colors = ListItemDefaults.colors(
                                                    //Set the background to transparent
                                                    containerColor = Color.Transparent
                                                ))
                                        }
                                    }
                                }
                                //A card with an outline for the total and checkout
                                OutlinedCard(modifier = Modifier.padding(8.dp)) {
                                    Row( //Row layout for the total and checkout section
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp)
                                            .height(36.dp)
                                    ) {
                                        Text(
                                            text = "Total: $",
                                            style = MaterialTheme.typography.titleLarge
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))

                                        //Need to import androidx.compose.animation.AnimatedContent
                                        //Utilize the default animation effect (crossfade) provided by Jetpack Compose.
                                        AnimatedContent( //Animate the total value
                                            targetState = state.total.toString(),
                                            label = ""
                                        ) {
                                            Text(
                                                text = it, //The total amount
                                                style = MaterialTheme.typography.titleLarge
                                            )
                                        }
                                        Spacer(modifier = Modifier.weight(1f))
                                        FilledTonalIconButton(onClick = {
                                            cartViewModel.checkout() //Triggers the checkout process
                                        }) {
                                            Icon( //The checkout button icon
                                                imageVector = Icons.TwoTone.ShoppingCartCheckout,
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
}