package com.vidv13.dashcode.sync

import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.WearableListenerService
import com.vidv13.dashcode.data.QrCodeRepository
import com.vidv13.dashcode.data.local.QrCode
import com.vidv13.dashcode.tile.DashCodeTileService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject

private val lenientJson = Json { ignoreUnknownKeys = true }

@AndroidEntryPoint
class QrCodeSyncService : WearableListenerService() {

    @Inject lateinit var repository: QrCodeRepository

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onDataChanged(events: DataEventBuffer) {
        events.forEach { event ->
            if (event.type == DataEvent.TYPE_CHANGED && event.dataItem.uri.path == DATA_PATH) {
                val dataMap = DataMapItem.fromDataItem(event.dataItem).dataMap
                val codesJson = dataMap.getString(KEY_CODES) ?: return@forEach
                val codes = lenientJson.decodeFromString<List<QrCode>>(codesJson)
                scope.launch {
                    repository.replaceAll(codes)
                    DashCodeTileService.requestUpdate(applicationContext)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }

    companion object {
        const val DATA_PATH = "/qr-codes"
        const val KEY_CODES = "codes"
    }
}
