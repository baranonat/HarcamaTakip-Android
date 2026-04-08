package com.baranonat.harcamatakip.data.remote

import retrofit2.http.GET

interface CurrencyService {
    // Ücretsiz döviz servisi
    @GET("latest?from=USD&to=TRY,EUR")
    suspend fun getRates(): CurrencyResponse
}

// İnternetten gelecek verinin kalıbı (Bunu da aynı dosyaya veya model paketine koyabilirsin)
data class CurrencyResponse(
    val rates: Map<String, Double>
)