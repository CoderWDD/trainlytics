package com.csd.trainlytics.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ExportImportState(
    val isExporting: Boolean = false,
    val exportSuccess: Boolean = false,
    val isImporting: Boolean = false,
    val importSuccess: Boolean = false
)

@HiltViewModel
class ExportImportViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(ExportImportState())
    val state: StateFlow<ExportImportState> = _state

    fun exportData() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isExporting = true, exportSuccess = false)
            delay(1500)
            _state.value = _state.value.copy(isExporting = false, exportSuccess = true)
        }
    }

    fun pickImportFile() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isImporting = true, importSuccess = false)
            delay(1500)
            _state.value = _state.value.copy(isImporting = false, importSuccess = true)
        }
    }
}
