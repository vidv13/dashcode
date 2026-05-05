package com.vidv13.dashcode.mobile.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import com.vidv13.dashcode.mobile.data.PhoneQrCode
import com.vidv13.dashcode.mobile.data.PhoneQrCodeStore
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class QrCodeViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val store: PhoneQrCodeStore,
) : ViewModel() {

    val codes: StateFlow<List<PhoneQrCode>> = store.codes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun addCode(name: String, content: String) {
        viewModelScope.launch {
            val updated = codes.value + PhoneQrCode(name = name.trim(), content = content.trim())
            store.save(updated)
            syncToWatch(updated)
        }
    }

    fun deleteCode(code: PhoneQrCode) {
        viewModelScope.launch {
            val updated = codes.value - code
            store.save(updated)
            syncToWatch(updated)
        }
    }

    private suspend fun syncToWatch(codes: List<PhoneQrCode>) {
        val request = PutDataMapRequest.create("/qr-codes").apply {
            dataMap.putString("codes", Json.encodeToString(codes))
            dataMap.putLong("timestamp", System.currentTimeMillis())
        }.asPutDataRequest().setUrgent()
        runCatching { Wearable.getDataClient(context).putDataItem(request).await() }
    }
}
