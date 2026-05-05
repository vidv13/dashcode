package com.vidv13.dashcode.mobile.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "qr_codes")
private val KEY = stringPreferencesKey("codes_json")

@Singleton
class PhoneQrCodeStore @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    val codes: Flow<List<PhoneQrCode>> = context.dataStore.data
        .map { prefs -> prefs[KEY]?.let { Json.decodeFromString(it) } ?: emptyList() }

    suspend fun save(codes: List<PhoneQrCode>) {
        context.dataStore.edit { it[KEY] = Json.encodeToString(codes) }
    }
}
