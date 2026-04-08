package com.baranonat.harcamatakip.ui.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator

import androidx.compose.material3.MaterialTheme
import com.baranonat.harcamatakip.R
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.baranonat.harcamatakip.ui.navigation.Screen
import kotlinx.coroutines.delay




@Composable
fun SplashScreen(navController: NavController) {

    // 1. GEÇİŞ MANTIĞI (Animasyonsuz, 3 saniye sonra List'e git)
    LaunchedEffect(key1 = true) {
        delay(3000) // 3 saniye ekranda kal
        navController.popBackStack() // Splash'i kapat
        navController.navigate("onboarding_screen") // Ana listeye uçur
    }

    // 2. GÖRSEL TASARIM (Sadece bir logo ve bir yazı)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer), // Açık mavi arka plan
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            // SABİT FOTOĞRAF (Az önce drawable'a attığımız logo)
            Image(
                painter = painterResource(id = R.drawable.ic_logo), // Dosya adı R.drawable.ic_logo olmalı
                contentDescription = "Uygulama Logosu",
                modifier = Modifier.size(150.dp) // Logonun boyutu
            )

            Spacer(modifier = Modifier.height(24.dp)) // Logo ile yazı arasına boşluk

            Text(
                text = "Harcama Rehberim",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}