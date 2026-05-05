package com.vidv13.dashcode.data

import com.vidv13.dashcode.data.local.QrCode
import com.vidv13.dashcode.data.local.QrCodeDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class QrCodeRepositoryImpl @Inject constructor(
    private val dao: QrCodeDao,
) : QrCodeRepository {

    override fun getAll(): Flow<List<QrCode>> = dao.getAll()

    override suspend fun getById(id: Long): QrCode? = dao.getById(id)

    override suspend fun replaceAll(codes: List<QrCode>) {
        dao.deleteAll()
        dao.insertAll(codes)
    }
}
