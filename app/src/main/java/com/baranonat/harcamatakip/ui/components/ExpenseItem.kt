package com.baranonat.harcamatakip.ui.components



import kotlin.Pair
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.baranonat.harcamatakip.data.local.Expense

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons

import androidx.compose.runtime.remember

import java.text.SimpleDateFormat
import java.util.*



@Composable
fun ExpenseItem(expense: Expense, onDelete: () -> Unit) {
    // 🎨 Kategoriye göre Emoji ve Renk belirleyen yardımcıyı çağırıyoruz
    val (kategoriEmoji, kategoriRengi) = getCategoryDetails(expense.kategori)

    // 📅 Tarihi "04.04.2026" formatına çeviriyoruz
    val formatliTarih = remember(expense.tarih) {
        val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        sdf.format(Date(expense.tarih))
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp), // Kartlar arası hafif boşluk
        color = Color.White,
        shape = RoundedCornerShape(24.dp),
        shadowElevation = 2.dp // Hafif gölge derinlik katar
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                // --- EMOJI KUTUCUĞU (Renkli Arka Planlı) ---
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .background(kategoriRengi.copy(alpha = 0.12f), CircleShape), // Soft renk
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = kategoriEmoji, fontSize = 24.sp)
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = expense.isim,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF1A1A1A),
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${expense.kategori} • $formatliTarih",
                        color = Color.Gray,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }

            // --- MİKTAR VE SİLME BUTONU ---
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "₺${String.format("%,.0f", expense.miktar)}",
                    fontWeight = FontWeight.Black,
                    fontSize = 17.sp,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.width(12.dp))

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier
                        .size(36.dp)
                        .background(Color(0xFFFFEBEE), CircleShape) // Silme butonu hafif kırmızımsı
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Delete,
                        contentDescription = "Sil",
                        tint = Color(0xFFEF5350), // Silme ikonu net kırmızı
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

// --- KATEGORİ DETAYLARI (DÜZELTİLMİŞ) ---
fun getCategoryDetails(kategori: String): Pair<String, Color> {
    return when (kategori) {
        "Mutfak"   -> "🍴" to Color(0xFFFFB74D) // Turuncu
        "Ulaşım"   -> "🚗" to Color(0xFF4FC3F7) // Mavi
        "Eğlence"  -> "🎮" to Color(0xFFBA68C8) // Mor
        "Fatura"   -> "📜" to Color(0xFFEF5350) // Kırmızı (Hata buradaydı, 'to' yaptık)
        "Giyim"    -> "👕" to Color(0xFF81C784) // Yeşil
        "Diğer"    -> "💰" to Color(0xFF90A4AE) // Gri
        else       -> "💵" to Color(0xFFB0BEC5) // Varsayılan
    }
}