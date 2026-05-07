package com.vidv13.dashcode.data

import com.vidv13.dashcode.data.local.QrCode
import kotlinx.coroutines.flow.Flow

interface QrCodeRepository {
    fun getAll(): Flow<List<QrCode>>
    suspend fun getById(id: Long): QrCode?
    suspend fun replaceAll(codes: List<QrCode>)
}
