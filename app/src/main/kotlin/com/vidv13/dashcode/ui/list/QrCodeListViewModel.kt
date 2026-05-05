package com.vidv13.dashcode.ui.list

import androidx.lifecycle.ViewModel
import com.vidv13.dashcode.data.QrCodeRepository
import com.vidv13.dashcode.data.local.QrCode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted

@HiltViewModel
class QrCodeListViewModel @Inject constructor(
    repository: QrCodeRepository,
) : ViewModel() {

    val codes: StateFlow<List<QrCode>> = repository.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
}
