package com.yschang.delicacy.compoents

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage  //coil
import coil.request.ImageRequest  //coil

//This Composable is to asynchronously load and display images through the AsyncImage component
//The MyImage function uses the Coil third-party library to load and display images
@Composable
fun MyImage(image: String, modifier: Modifier = Modifier) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current) //Gets the current context
            .data(image) //Specifies the source of the image (URL)
            .crossfade(true) //Enables a crossfade animation
            .build(),
        contentDescription = null,
        //Defines image scaling using ContentScale.Crop to fit and crop within bounds
        contentScale = ContentScale.Crop,
        modifier = modifier //Apply a modifier to control the appearance and behavior of the component
    )
}