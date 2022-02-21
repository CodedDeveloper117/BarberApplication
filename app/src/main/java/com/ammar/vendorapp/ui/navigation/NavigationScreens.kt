package com.ammar.vendorapp.ui.navigation

sealed class NavigationScreens(
    val route: String,
    val title: String,
) {

    object Login : NavigationScreens(
        "login", "Login"
    )

    object Signup: NavigationScreens(
        "sign-up", "Signup"
    )

    object Home: NavigationScreens(
        "home", "Home"
    )
}

val screenItems = listOf(
    NavigationScreens.Login,
    NavigationScreens.Signup,
    NavigationScreens.Home
)