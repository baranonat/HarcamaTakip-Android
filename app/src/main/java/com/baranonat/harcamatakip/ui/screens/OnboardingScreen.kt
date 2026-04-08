package com.baranonat.harcamatakip.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.baranonat.harcamatakip.data.UserPrefManager
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(navController: NavController) {
    val pagerState = rememberPagerState(pageCount = { 3 })
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {

        // SAYFALAR (Tanıtım İçeriği)
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            OnboardingContent(pageIndex = page)
        }

        // ALT KONTROLLER
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 40.dp)
                .align(Alignment.BottomCenter)
        ) {
            if (pagerState.currentPage < 2) {
                // NOKTALAR (TAM MERKEZDE)
                Row(
                    modifier = Modifier.align(Alignment.Center), // Box'ın tam ortasına hizalar
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    repeat(3) { iteration ->
                        val isSelected = pagerState.currentPage == iteration
                        Box(
                            modifier = Modifier
                                .size(if (isSelected) 10.dp else 8.dp)
                                .clip(CircleShape)
                                .background(if (isSelected) Color.Black else Color.LightGray)
                        )
                    }
                }

                // GEÇ BUTONU (EN SAĞDA)
                TextButton(
                    modifier = Modifier.align(Alignment.CenterEnd), // Box'ın sağ ucuna hizalar
                    onClick = {
                        navController.navigate("list_screen") {
                            popUpTo("onboarding_screen") { inclusive = true }
                        }
                    }
                ) {
                    Text("Geç", color = Color.Gray, fontWeight = FontWeight.SemiBold)
                }
            } else {
                // --- SON SAYFA: Sadece Başla Butonu ---
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    onClick = {
                        navController.navigate("list_screen") {
                            popUpTo("onboarding_screen") { inclusive = true }
                        }
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                ) {
                    Text("Hadi Başlayalım", color = Color.White, fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
fun OnboardingContent(pageIndex: Int) {
    val titles = listOf("Harcamalarını Takip Et", "Kategorilere Ayır", "Raporları İncele")
    val descriptions = listOf(
        "Günlük harcamalarını kolayca ekle ve yönet.",
        "Giderlerini mutfak, ulaşım, eğlence gibi grupla.",
        "Ay sonunda nereye ne kadar harcadığını net gör."
    )
    val icons = listOf(Icons.Default.ShoppingCart, Icons.Default.List, Icons.Default.DateRange)

    Column(
        modifier = Modifier.fillMaxSize().padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icons[pageIndex],
            contentDescription = null,
            modifier = Modifier.size(120.dp),
            tint = Color.Black
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = titles[pageIndex],
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = descriptions[pageIndex],
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = Color.Gray
        )
    }
}