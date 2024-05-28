package com.yschang.delicacy.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.twotone.ArrowBack
import androidx.compose.material.icons.twotone.Favorite
import androidx.compose.material.icons.twotone.FavoriteBorder
import androidx.compose.material.icons.twotone.ShoppingCart
import androidx.compose.material.icons.twotone.ShoppingCartCheckout
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yschang.delicacy.MyApplication
import com.yschang.delicacy.compoents.MyImage
import com.yschang.delicacy.viewmodel.FoodDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodDetailScreen( /***** Food Detail (Description) Screen *****/
    onBack: () -> Unit, //Handle back navigation
    foodDetailViewModel: FoodDetailViewModel = viewModel()
) {
    val food = remember {
        MyApplication.currentFood //The current food item
    }
    val state by foodDetailViewModel.state.collectAsState() //Collect the state from the ViewModel
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(text = food.name)
            }, navigationIcon = {
                IconButton(onClick = { onBack() }) { //Back button action
                    Icon( //The back arrow "<-" icon
                        imageVector = Icons.AutoMirrored.TwoTone.ArrowBack,
                        contentDescription = null
                    )
                }
            }, actions = {
                IconButton(onClick = {
                    foodDetailViewModel.favorite()
                }) {
                    Icon(
                        imageVector =
                        if (state.isFavorite)
                            Icons.TwoTone.Favorite
                        else Icons.TwoTone.FavoriteBorder, contentDescription = null
                    )
                }
            })
        }
    ) { paddingValues -> //The content area of the Scaffold
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues), //Applies padding from the Scaffold
            contentAlignment = Alignment.Center
        ) {
            AnimatedContent( //Animated content transition based on loading state
                state.isLoading, label = "" // Observes the loading state
            ) {
                if (it) { //If the content is loading
                    CircularProgressIndicator()
                } else { //When loading is false
                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        OutlinedCard( //Food image
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                                .height(200.dp),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            MyImage( //Custom component for displaying the food image
                                image = food.image,
                                modifier = Modifier
                                    .padding(16.dp)
                                    .clip(
                                        RoundedCornerShape(
                                            16.dp
                                        )
                                    )
                                    .fillMaxSize()
                            )
                        }
                        OutlinedCard( //Food description
                            modifier = Modifier
                                .padding(16.dp, 0.dp)
                                .weight(1f)
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                Modifier
                                    .padding(
                                        16.dp
                                    )
                                    .verticalScroll(rememberScrollState()),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Text( //Display the food price
                                    text = food.price,
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.tertiary
                                )
                                Text(text = food.description) //Display the food description
                            }
                        }
                        OutlinedCard( //An outlined card for the bottom controls
                            modifier = Modifier
                                .padding(16.dp)
                                .height(100.dp),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Spacer(modifier = Modifier.weight(1f))
                                FilledIconButton(onClick = {
                                    foodDetailViewModel.cart(food) //Action to add to cart
                                }) {
                                    Icon( //The shopping cart icon
                                        imageVector = Icons.TwoTone.ShoppingCart,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onPrimary
                                    )
                                }
                                FilledIconButton(onClick = { //Action to buy the food item
                                    foodDetailViewModel.buy()
                                }) {
                                    Icon( //The checkout icon
                                        imageVector = Icons.TwoTone.ShoppingCartCheckout,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onPrimary //The icon color
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