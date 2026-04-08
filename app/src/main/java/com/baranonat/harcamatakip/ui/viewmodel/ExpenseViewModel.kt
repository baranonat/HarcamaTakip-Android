package com.baranonat.harcamatakip.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baranonat.harcamatakip.data.local.Expense
import com.baranonat.harcamatakip.data.remote.CurrencyResponse
import com.baranonat.harcamatakip.data.repository.CurrencyRepository
import com.baranonat.harcamatakip.data.repository.ExpenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ExpenseViewModel @Inject constructor(
    private val repository: ExpenseRepository,
    private val currencyRepository: CurrencyRepository
) : ViewModel() {

    private val _expenses = MutableStateFlow<List<Expense>>(emptyList())
    val expenses: StateFlow<List<Expense>> = _expenses

    // Canlı Kur Değerleri (Double olarak tutuyoruz ki hesaplama yapalım)
    var usdRateNum by mutableStateOf(0.0)
    var eurRateNum by mutableStateOf(0.0)

    // UI'da görünecek metin halleri
    var usdRate by mutableStateOf("0.00")
    var eurRate by mutableStateOf("0.00")

    // Seçili para birimi: "TL", "USD", "EUR"
    var selectedCurrency by mutableStateOf("TL")

    val totalAmount: StateFlow<Double> = _expenses
        .map { list -> list.sumOf { it.miktar } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    init {
        viewModelScope.launch {
            repository.getAllExpenses().collectLatest { _expenses.value = it }
        }
        fetchRates()
    }

    private fun fetchRates() {
        viewModelScope.launch {
            val response = currencyRepository.getRates()
            response?.let {
                val tryVal = it.rates["TRY"] ?: 0.0
                val eurVal = it.rates["EUR"] ?: 1.0

                usdRateNum = tryVal
                eurRateNum = tryVal / eurVal

                usdRate = String.format("%.2f", usdRateNum)
                eurRate = String.format("%.2f", eurRateNum)
            }
        }
    }

    // --- DÖVİZ ÇEVİRİCİ FONKSİYON ---
    fun getFormattedTotal(total: Double): String {
        return when (selectedCurrency) {
            "USD" -> {
                val sonuc = if (usdRateNum > 0) total / usdRateNum else 0.0
                "$${String.format("%.2f", sonuc)}"
            }
            "EUR" -> {
                val sonuc = if (eurRateNum > 0) total / eurRateNum else 0.0
                "€${String.format("%.2f", sonuc)}"
            }
            else -> "₺${String.format("%,.0f", total)}"
        }
    }

    fun addExpense(expense: Expense) = viewModelScope.launch { repository.insertExpense(expense) }
    fun removeExpense(expense: Expense) = viewModelScope.launch { repository.deleteExpense(expense) }
}
