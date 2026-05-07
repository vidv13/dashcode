package com.vidv13.dashcode.ui.list

import com.vidv13.dashcode.data.QrCodeRepository
import com.vidv13.dashcode.data.local.QrCode
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class QrCodeListViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val repository: QrCodeRepository = mockk()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `codes starts with empty list before any subscriber`() = runTest {
        every { repository.getAll() } returns flowOf(emptyList())
        val viewModel = QrCodeListViewModel(repository)
        assertTrue(viewModel.codes.value.isEmpty())
    }

    @Test
    fun `codes emits list from repository when subscribed`() = runTest {
        val codes = listOf(QrCode(id = 1, name = "Gym", content = "gym"))
        every { repository.getAll() } returns flowOf(codes)
        val viewModel = QrCodeListViewModel(repository)
        // WhileSubscribed only collects upstream while there is an active subscriber
        backgroundScope.launch { viewModel.codes.collect {} }
        advanceUntilIdle()
        assertEquals(codes, viewModel.codes.value)
    }
}
