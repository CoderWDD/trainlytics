package com.csd.trainlytics.ui.body

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csd.trainlytics.domain.usecase.body.RecordBodyStatsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RecordBodyStatsUiState(
    val weightInput: String = "",
    val bodyFatInput: String = "",
    val waistInput: String = "",
    val note: String = "",
    val isSaving: Boolean = false,
    val saved: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class RecordBodyStatsViewModel @Inject constructor(
    private val recordBodyStats: RecordBodyStatsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(RecordBodyStatsUiState())
    val state: StateFlow<RecordBodyStatsUiState> = _state.asStateFlow()

    fun onWeightChange(value: String) {
        _state.update { it.copy(weightInput = value, error = null) }
    }

    fun onBodyFatChange(value: String) {
        _state.update { it.copy(bodyFatInput = value, error = null) }
    }

    fun onWaistChange(value: String) {
        _state.update { it.copy(waistInput = value, error = null) }
    }

    fun onNoteChange(value: String) {
        _state.update { it.copy(note = value) }
    }

    fun save() {
        val s = _state.value
        val weight = s.weightInput.toFloatOrNull()
        val bodyFat = s.bodyFatInput.toFloatOrNull()
        val waist = s.waistInput.toFloatOrNull()

        if (weight == null && bodyFat == null && waist == null) {
            _state.update { it.copy(error = "请至少填写一项数据") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isSaving = true, error = null) }
            try {
                recordBodyStats(
                    weightKg = weight,
                    bodyFatPercent = bodyFat,
                    waistCm = waist,
                    recordedAt = System.currentTimeMillis(),
                    note = s.note
                )
                _state.update { it.copy(isSaving = false, saved = true) }
            } catch (e: Exception) {
                _state.update { it.copy(isSaving = false, error = e.message ?: "保存失败") }
            }
        }
    }
}
