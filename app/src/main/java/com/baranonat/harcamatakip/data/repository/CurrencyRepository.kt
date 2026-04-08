package com.baranonat.harcamatakip.data.repository

import com.baranonat.harcamatakip.data.remote.CurrencyService
import javax.inject.Inject

class CurrencyRepository @Inject constructor(
    private val api: CurrencyService
) {
    suspend fun getRates() = try {
        api.getRates()
    } catch (e: Exception) {
        null
    }
}