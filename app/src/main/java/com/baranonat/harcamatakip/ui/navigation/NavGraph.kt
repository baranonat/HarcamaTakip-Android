package com.baranonat.harcamatakip.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.baranonat.harcamatakip.ui.screens.ListScreen
import com.baranonat.harcamatakip.ui.screens.OnboardingScreen
import com.baranonat.harcamatakip.ui.screens.SplashScreen

@Composable
fun SetupNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route // Uygulama hangi sayfa ile açılacak? (Liste Sayfası)
    ) {

        composable(route = Screen.Splash.route){
            SplashScreen(navController = navController)
        }

        composable(route = Screen.Onboarding.route){
            OnboardingScreen(navController =navController)
        }
        // 1. DURAK: Liste Ekranı
        composable(route = Screen.List.route) {
            // Şimdilik burası boş, birazdan ListScreen() gelecek
            ListScreen(navController=navController)
        }



    }
}