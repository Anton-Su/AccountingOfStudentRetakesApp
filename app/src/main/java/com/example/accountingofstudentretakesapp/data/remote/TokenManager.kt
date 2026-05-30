package com.example.accountingofstudentretakesapp.data.remote

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.tokenDataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_tokens")

class TokenManager(private val context: Context) {

    private val ACCESS_TOKEN = stringPreferencesKey("access_token")

    suspend fun saveAccessToken(token: String) {
        context.tokenDataStore.edit { it[ACCESS_TOKEN] = token }
    }

    suspend fun clearTokens() {
        context.tokenDataStore.edit { it.clear() }
    }
}