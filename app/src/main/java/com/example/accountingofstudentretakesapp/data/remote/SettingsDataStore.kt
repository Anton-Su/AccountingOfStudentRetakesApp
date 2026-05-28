package com.example.accountingofstudentretakesapp.data.remote

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.accountingofstudentretakesapp.domain.model.UserDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "settings")

class SettingsDataStore(private val context: Context) {
    companion object {
        val USER_ID = longPreferencesKey("user_id")
        val ROLE = stringPreferencesKey("role")
        val FIRST_NAME = stringPreferencesKey("first_name")
        val SECOND_NAME = stringPreferencesKey("second_name")
        val LAST_NAME = stringPreferencesKey("last_name")
        val GENDER = stringPreferencesKey("gender")
        val AGE = intPreferencesKey("age")
        val EMAIL = stringPreferencesKey("email")
        val PASSWORD_HASH = stringPreferencesKey("password_hash")
    }

    val userIdFlow: Flow<Long> = context.dataStore.data
        .map { prefs: Preferences ->
            prefs[USER_ID] ?: 0L
        }

    val roleFlow: Flow<String> = context.dataStore.data
        .map { prefs: Preferences ->
            prefs[ROLE] ?: ""
        }

    val firstNameFlow: Flow<String> = context.dataStore.data
        .map { prefs: Preferences ->
            prefs[FIRST_NAME] ?: ""
        }

    val secondNameFlow: Flow<String> = context.dataStore.data
        .map { prefs: Preferences ->
            prefs[SECOND_NAME] ?: ""
        }

    val lastNameFlow: Flow<String> = context.dataStore.data
        .map { prefs: Preferences ->
            prefs[LAST_NAME] ?: ""
        }

    val genderFlow: Flow<String> = context.dataStore.data
        .map { prefs: Preferences ->
            prefs[GENDER] ?: "female"
        }

    val ageFlow: Flow<Int> = context.dataStore.data
        .map { prefs: Preferences ->
            prefs[AGE] ?: 18
        }

    val emailFlow: Flow<String> = context.dataStore.data
        .map { prefs: Preferences ->
            prefs[EMAIL] ?: ""
        }

    val passwordHashFlow: Flow<String> = context.dataStore.data
        .map { prefs: Preferences ->
            prefs[PASSWORD_HASH] ?: ""
        }

    suspend fun setUserId(value: Long) {
        context.dataStore.edit { prefs ->
            prefs[USER_ID] = value
        }
    }

    suspend fun setRole(value: String) {
        context.dataStore.edit { prefs ->
            prefs[ROLE] = value
        }
    }

    suspend fun setFirstName(value: String) {
        context.dataStore.edit { prefs ->
            prefs[FIRST_NAME] = value
        }
    }

    suspend fun setSecondName(value: String) {
        context.dataStore.edit { prefs ->
            prefs[SECOND_NAME] = value
        }
    }

    suspend fun setLastName(value: String) {
        context.dataStore.edit { prefs ->
            prefs[LAST_NAME] = value
        }
    }

    suspend fun setGender(value: String) {
        context.dataStore.edit { prefs ->
            prefs[GENDER] = value
        }
    }

    suspend fun setAge(value: Int) {
        context.dataStore.edit { prefs ->
            prefs[AGE] = value
        }
    }

    suspend fun setEmail(value: String) {
        context.dataStore.edit { prefs ->
            prefs[EMAIL] = value
        }
    }

    suspend fun setPasswordHash(value: String) {
        context.dataStore.edit { prefs ->
            prefs[PASSWORD_HASH] = value
        }
    }

    suspend fun saveUserProfile(user: UserDto) {
        context.dataStore.edit { prefs ->
            prefs[USER_ID] = user.id
            prefs[ROLE] = user.role.name
            prefs[FIRST_NAME] = user.firstName
            prefs[SECOND_NAME] = user.secondName
            prefs[LAST_NAME] = user.lastName
            prefs[GENDER] = user.gender
            prefs[AGE] = user.age
            prefs[EMAIL] = user.email
        }
    }

    suspend fun clearUserData() {
        context.dataStore.edit { prefs ->
            prefs.clear()
        }
    }
}