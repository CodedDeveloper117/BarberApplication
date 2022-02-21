package com.ammar.vendorapp.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.ammar.vendorapp.ui.theme.AmethystPurple

@Composable
fun HomeScreen(
    navController: NavController
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AmethystPurple)
    ) {
        Text(
            text = "Welcome",
            style = MaterialTheme.typography.h1.copy(color = Color.White),
            modifier = Modifier.align(
                Alignment.Center
            )
        )
    }

}