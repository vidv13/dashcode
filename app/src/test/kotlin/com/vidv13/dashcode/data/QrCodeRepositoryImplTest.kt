package com.vidv13.dashcode.data

import com.vidv13.dashcode.data.local.QrCode
import com.vidv13.dashcode.data.local.QrCodeDao
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class QrCodeRepositoryImplTest {

    private val dao: QrCodeDao = mockk(relaxed = true)
    private val repo = QrCodeRepositoryImpl(dao)

    private val sampleCodes = listOf(
        QrCode(id = 1, name = "Gym", content = "gym-content"),
        QrCode(id = 2, name = "Daycare", content = "daycare-content"),
    )

    @Test
    fun `getAll delegates to dao`() = runTest {
        every { dao.getAll() } returns flowOf(sampleCodes)
        val result = repo.getAll().first()
        assertEquals(sampleCodes, result)
    }

    @Test
    fun `getById delegates to dao`() = runTest {
        coEvery { dao.getById(1L) } returns sampleCodes[0]
        val result = repo.getById(1L)
        assertEquals(sampleCodes[0], result)
    }

    @Test
    fun `replaceAll deletes then inserts`() = runTest {
        repo.replaceAll(sampleCodes)
        coVerify(ordering = io.mockk.Ordering.ORDERED) {
            dao.deleteAll()
            dao.insertAll(sampleCodes)
        }
    }
}
