package com.vidv13.dashcode.ui.detail

import android.graphics.Bitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vidv13.dashcode.data.QrCodeRepository
import com.vidv13.dashcode.util.QrBitmapGenerator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DetailUiState(
    val name: String = "",
    val bitmap: Bitmap? = null,
)

@HiltViewModel
class QrCodeDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: QrCodeRepository,
) : ViewModel() {

    private val codeId: Long = checkNotNull(savedStateHandle["codeId"])

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState

    init {
        viewModelScope.launch {
            val code = repository.getById(codeId) ?: return@launch
            val bitmap = QrBitmapGenerator.generate(code.content, QR_SIZE_PX)
            _uiState.value = DetailUiState(name = code.name, bitmap = bitmap)
        }
    }

    companion object {
        private const val QR_SIZE_PX = 300
    }
}
