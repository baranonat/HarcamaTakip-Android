package com.baranonat.harcamatakip.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.baranonat.harcamatakip.data.UserPrefManager
import com.baranonat.harcamatakip.data.local.Expense
import com.baranonat.harcamatakip.ui.components.ExpenseItem
import com.baranonat.harcamatakip.ui.viewmodel.ExpenseViewModel

import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(navController: NavController, viewModel: ExpenseViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val userPrefManager = remember { UserPrefManager(context) }
    val scope = rememberCoroutineScope()

    // --- VERİ GÖZLEMLEME ---
    val kayitliIsim by userPrefManager.userName.collectAsState(initial = "YÜKLENİYOR")
    val expenses by viewModel.expenses.collectAsState()
    val totalAmount by viewModel.totalAmount.collectAsState()

    // --- STATE'LER (Arama, Panel, Silme) ---
    var searchQuery by remember { mutableStateOf("") }
    var showNameSheet by remember { mutableStateOf(false) }
    var showAddExpenseSheet by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var expenseToDelete by remember { mutableStateOf<Expense?>(null) }

    // --- FİLTRELEME MANTIĞI (Arama Çubuğu İçin) ---
    val filteredExpenses = remember(searchQuery, expenses) {
        if (searchQuery.isEmpty()) expenses
        else expenses.filter { it.isim.contains(searchQuery, ignoreCase = true) || it.kategori.contains(searchQuery, ignoreCase = true) }
    }

    LaunchedEffect(kayitliIsim) {
        if (kayitliIsim == null) showNameSheet = true
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddExpenseSheet = true },
                containerColor = Color.Black,
                shape = CircleShape,
                elevation = FloatingActionButtonDefaults.elevation(8.dp)
            ) { Icon(Icons.Default.Add, contentDescription = null, tint = Color.White) }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFFAFAFA))
        ) {
            // --- 1. ÜST KISIM (HOŞ GELDİNİZ) ---
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Hoş Geldiniz,", color = Color.Gray, fontSize = 14.sp)
                    Text(
                        text = if (kayitliIsim == "YÜKLENİYOR" || kayitliIsim == null) "Kullanıcı" else kayitliIsim!!,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
                IconButton(onClick = {}, modifier = Modifier.background(Color.White, CircleShape)) {
                    Icon(Icons.Rounded.Notifications, contentDescription = null)
                }
            }

            // --- 2. SİYAH KART (BAKİYE & KURLAR) ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .height(200.dp)
                    .background(
                        Brush.linearGradient(listOf(Color(0xFF2D2D2D), Color(0xFF000000))),
                        RoundedCornerShape(32.dp)
                    )
                    .padding(28.dp)
            ) {
                Column(modifier = Modifier.align(Alignment.CenterStart)) {
                    Text("Toplam Harcama (${viewModel.selectedCurrency})", color = Color.White.copy(alpha = 0.6f))
                    Text(
                        text = viewModel.getFormattedTotal(totalAmount),
                        color = Color.White,
                        fontSize = 38.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CurrencyChip("🇹🇷 TRY", viewModel.selectedCurrency == "TL") { viewModel.selectedCurrency = "TL" }
                        Spacer(modifier = Modifier.width(8.dp))
                        CurrencyChip("🇺🇸 USD: ${viewModel.usdRate}", viewModel.selectedCurrency == "USD") { viewModel.selectedCurrency = "USD" }
                        Spacer(modifier = Modifier.width(8.dp))
                        CurrencyChip("🇪🇺 EUR: ${viewModel.eurRate}", viewModel.selectedCurrency == "EUR") { viewModel.selectedCurrency = "EUR" }
                    }
                }
            }

            // --- 3. ARAMA ÇUBUĞU ---
            // --- ARAMA ÇUBUĞU (GÜNCEL MATERİAL 3 FORMATI) ---
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 16.dp),
                placeholder = { Text("Harcama ara...", color = Color.Gray) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
                shape = RoundedCornerShape(16.dp),
                singleLine = true,
                // HATALI KISIM BURASIYDI, ŞU ŞEKİLDE DÜZELTTİK:
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color.White,
                    focusedBorderColor = Color.Black.copy(alpha = 0.2f),
                    unfocusedBorderColor = Color.Transparent,
                )
            )

            // --- 4. ÖZET İSTATİSTİK ---
            if (expenses.isNotEmpty()) {
                val enCokKategori = expenses.groupBy { it.kategori }.maxByOrNull { it.value.sumOf { exp -> exp.miktar } }?.key
                Surface(
                    modifier = Modifier.padding(horizontal = 24.dp).padding(bottom = 16.dp),
                    color = Color.Black.copy(alpha = 0.04f),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text("💡", fontSize = 18.sp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "En çok harcama: $enCokKategori",
                            fontSize = 13.sp, color = Color.DarkGray, fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // --- 5. LİSTE VE BOŞ DURUM ---
            Text("Son İşlemler", modifier = Modifier.padding(horizontal = 24.dp), fontWeight = FontWeight.Bold, fontSize = 18.sp)

            if (filteredExpenses.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredExpenses) { expense ->
                        ExpenseItem(expense = expense) {
                            expenseToDelete = expense
                            showDeleteDialog = true
                        }
                    }
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxSize().padding(bottom = 50.dp),
                    horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center
                ) {
                    Text("🔍", fontSize = 40.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(if(searchQuery.isEmpty()) "Henüz harcama yok" else "Sonuç bulunamadı", color = Color.Gray, fontWeight = FontWeight.Bold)
                }
            }
        }

        // --- SİLME ONAY DİYALOĞU ---
        if (showDeleteDialog && expenseToDelete != null) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Harcamayı Sil", fontWeight = FontWeight.Bold) },
                text = { Text("'${expenseToDelete?.isim}' silinsin mi?") },
                confirmButton = {
                    TextButton(onClick = {
                        expenseToDelete?.let { viewModel.removeExpense(it) }
                        showDeleteDialog = false
                    }, colors = ButtonDefaults.textButtonColors(contentColor = Color.Red)) { Text("Sil", fontWeight = FontWeight.Bold) }
                },
                dismissButton = { TextButton(onClick = { showDeleteDialog = false }) { Text("Vazgeç") } },
                containerColor = Color.White, shape = RoundedCornerShape(24.dp)
            )
        }

        // --- MODAL SHEETS (İsim & Ekleme) ---
        if (showNameSheet) {
            ModalBottomSheet(onDismissRequest = { }, containerColor = Color.White) {
                NameInputSheetContent { ad -> scope.launch { userPrefManager.saveName(ad); showNameSheet = false } }
            }
        }
        if (showAddExpenseSheet) {
            ModalBottomSheet(onDismissRequest = { showAddExpenseSheet = false }, containerColor = Color.White) {
                AddExpensePanelContent { m, i, k ->
                    viewModel.addExpense(Expense(0, i, m, k, System.currentTimeMillis()))
                    showAddExpenseSheet = false
                }
            }
        }
    }
}

// --- CURRENCY CHIP ---
@Composable
fun CurrencyChip(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) Color.White.copy(alpha = 0.25f) else Color.White.copy(alpha = 0.1f)
    ) {
        Text(label, modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp), color = if (isSelected) Color.Cyan else Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
    }
}
// --- HARCAMA EKLEME PANELİ (ALTTA AÇILAN) ---
@Composable
fun AddExpensePanelContent(onSave: (Double, String, String) -> Unit) {
    val context = LocalContext.current
    var miktar by remember { mutableStateOf("") }
    var isim by remember { mutableStateOf("") }
    var secilenKategori by remember { mutableStateOf("Mutfak") }
    val kategoriler = listOf("Mutfak" to "🍴", "Ulaşım" to "🚗", "Eğlence" to "🎮", "Fatura" to "📜", "Giyim" to "👕", "Diğer" to "💰")

    Column(modifier = Modifier.padding(24.dp).padding(bottom = 32.dp)) {
        Text("Yeni Harcama", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = miktar, onValueChange = { if (it.all { c -> c.isDigit() || c == '.' }) miktar = it }, label = { Text("Miktar (₺)") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp))
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(value = isim, onValueChange = { isim = it }, label = { Text("Başlık") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp))
        Spacer(modifier = Modifier.height(20.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(kategoriler) { (ad, emoji) ->
                val isSelected = secilenKategori == ad
                Surface(modifier = Modifier.clickable { secilenKategori = ad }, shape = RoundedCornerShape(12.dp), color = if (isSelected) Color.Black else Color(0xFFF1F1F1)) {
                    Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) { Text(emoji); Spacer(modifier = Modifier.width(6.dp)); Text(ad, color = if (isSelected) Color.White else Color.Black) }
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = {
            val m = miktar.toDoubleOrNull()
            if (isim.isNotBlank() && m != null && m > 0) onSave(m, isim, secilenKategori)
            else Toast.makeText(context, "Geçersiz giriş!", Toast.LENGTH_SHORT).show()
        }, modifier = Modifier.fillMaxWidth().height(56.dp), colors = ButtonDefaults.buttonColors(containerColor = Color.Black), shape = RoundedCornerShape(16.dp)) { Text("Kaydet", color = Color.White) }
    }
}

@Composable
fun NameInputSheetContent(onSave: (String) -> Unit) {
    var ad by remember { mutableStateOf("") }
    Column(modifier = Modifier.padding(24.dp).padding(bottom = 32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Adın Nedir?", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = ad, onValueChange = { ad = it }, label = { Text("Adınız") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp))
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = { if (ad.isNotEmpty()) onSave(ad) }, modifier = Modifier.fillMaxWidth().height(56.dp), colors = ButtonDefaults.buttonColors(containerColor = Color.Black), shape = RoundedCornerShape(16.dp)) { Text("Başla", color = Color.White) }
    }
}