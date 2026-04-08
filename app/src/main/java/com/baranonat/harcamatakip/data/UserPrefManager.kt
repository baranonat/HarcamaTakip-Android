package com.baranonat.harcamatakip.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// 1. Dosyanın adını belirliyoruz: "user_prefs"
private val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserPrefManager(private val context: Context) {

    companion object {
        // 2. Çekmecedeki etiketin adı: "user_name"
        val USER_NAME_KEY = stringPreferencesKey("user_name")
    }

    // 3. İSMİ KAYDETME FONKSİYONU (Yazma işlemi)
    suspend fun saveName(name: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_NAME_KEY] = name
        }
    }

    // 4. İSMİ OKUMA FONKSİYONU (Okuma işlemi)
    val userName: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[USER_NAME_KEY]
    }
}