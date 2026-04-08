package com.baranonat.harcamatakip.ui.navigation

sealed class Screen(val route: String) {
    object Splash:Screen("splash_screen")
    object List : Screen("list_screen")   // Harcamaların listelendiği ana sayfa
    object Onboarding: Screen(route = "onboarding_screen")
}