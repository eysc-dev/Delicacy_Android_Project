package com.yschang.delicacy.screen.home

import android.widget.Toast //Show toast messages
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.twotone.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.yschang.delicacy.MyApplication
import com.yschang.delicacy.viewmodel.home.CartViewModel
import com.yschang.delicacy.viewmodel.home.DetailViewModel

//Enables experimental Material3 features
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen( /***** Food Description Screen *****/
    detailViewModel: DetailViewModel = viewModel(),
    cartViewModel: CartViewModel = viewModel(),
    onBack: () -> Unit //Handle the back action
) {
    val food = MyApplication.food //Retrieve the food item
    val context = LocalContext.current //Get the current context
    Scaffold(
        topBar = { //The top app bar with title and navigation icon
            TopAppBar(title = {
                Text(text = food.name) //Set the title to the food item name
            }, navigationIcon = { //Navigation icon for the top app bar
                IconButton(onClick = {
                    onBack() //Button to handle back navigation
                }) {
                    Icon( //The icon for the back button
                        imageVector = Icons.AutoMirrored.TwoTone.ArrowBack,
                        contentDescription = null
                    )
                }
            })
        }, contentWindowInsets = WindowInsets(0)
    ) {
        Column( //Use a vertical column layout
            Modifier.padding(it)
        ) {
            Box {
                AsyncImage( //Display an image asynchronously
                    model = ImageRequest.Builder(LocalContext.current) //Create an image request
                        .data(food.image) //Set the image source to the food item's image
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop, //Scale the image by cropping
                    modifier = Modifier //Modifier for customizing the layout
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(16.dp)
                        .clip(RoundedCornerShape(16.dp)) //Clip the image with rounded corners
                )
            }
            OutlinedCard(modifier = Modifier.padding(16.dp)) {
                Column {
                    ListItem(headlineContent = { //List item for the card header
                        Text(
                            text = "Description:", //The label for the description
                            style = MaterialTheme.typography.titleLarge
                        )
                    })
                    ListItem(headlineContent = { //List item for the card content
                        Text(text = food.description) //Display the food item description
                    })
                }
            }
            Spacer(modifier = Modifier.weight(1f)) //The label for the description
            ElevatedCard( //Elevated card for the button row
                modifier = Modifier
                    .height(200.dp)
                    .padding(16.dp)
            ) {
                //Use a horizontal row layout
                Row(
                    Modifier.padding(16.dp)
                ) {
                    Spacer(modifier = Modifier.weight(1f)) //Spacer to push buttons to the right
                    ElevatedButton(
                        onClick = { //Action when the button is clicked
                        //Show a toast message
                        Toast.makeText(context, "Add to cart successfully", Toast.LENGTH_SHORT)
                            .show()
                        detailViewModel.addToCart(food) //Call the ViewModel to add the food item to the cart
                        cartViewModel.refresh()
                        }
                    ) {
                        Text(text = "Add to Cart")
                    }
                    Spacer(modifier = Modifier.width(16.dp)) //Add horizontal space between buttons
                    Button(onClick = { /*TODO*/ }) {
                        Text(text = "Buy Now")
                    }
                }
            }
        }
    }
}