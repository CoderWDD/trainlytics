package com.csd.trainlytics.ui.body

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csd.trainlytics.domain.model.BodyRecord
import com.csd.trainlytics.domain.repository.BodyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

data class BodyEntryState(
    val weightKg: String = "",
    val bodyFatPercent: String = "",
    val waistCm: String = "",
    val notes: String = "",
    val weightKgFloat: Float = 70f,
    val bodyFatFloat: Float = 15f,
    val waistCmFloat: Float = 80f,
    val isSaving: Boolean = false,
    val isSaved: Boolean = false
)

@HiltViewModel
class BodyViewModel @Inject constructor(
    private val bodyRepository: BodyRepository
) : ViewModel() {

    private val _entryState = MutableStateFlow(BodyEntryState())
    val entryState: StateFlow<BodyEntryState> = _entryState.asStateFlow()

    fun updateWeight(v: String) { _entryState.value = _entryState.value.copy(weightKg = v, weightKgFloat = v.toFloatOrNull() ?: _entryState.value.weightKgFloat) }
    fun updateBodyFat(v: String) { _entryState.value = _entryState.value.copy(bodyFatPercent = v, bodyFatFloat = v.toFloatOrNull() ?: _entryState.value.bodyFatFloat) }
    fun updateWaist(v: String) { _entryState.value = _entryState.value.copy(waistCm = v, waistCmFloat = v.toFloatOrNull() ?: _entryState.value.waistCmFloat) }
    fun updateNotes(v: String) { _entryState.value = _entryState.value.copy(notes = v) }

    fun updateWeightFloat(v: Float) { _entryState.value = _entryState.value.copy(weightKgFloat = v, weightKg = "%.1f".format(v)) }
    fun updateBodyFatFloat(v: Float) { _entryState.value = _entryState.value.copy(bodyFatFloat = v, bodyFatPercent = "%.1f".format(v)) }
    fun updateWaistFloat(v: Float) { _entryState.value = _entryState.value.copy(waistCmFloat = v, waistCm = "%.1f".format(v)) }

    fun saveRecord(onSaved: () -> Unit) {
        val s = _entryState.value
        if (s.weightKg.isBlank() && s.bodyFatPercent.isBlank() && s.waistCm.isBlank()) return
        viewModelScope.launch {
            _entryState.value = s.copy(isSaving = true)
            bodyRepository.saveBodyRecord(
                BodyRecord(
                    date = LocalDate.now(),
                    timestamp = LocalDateTime.now(),
                    weightKg = s.weightKg.toFloatOrNull(),
                    bodyFatPercent = s.bodyFatPercent.toFloatOrNull(),
                    waistCm = s.waistCm.toFloatOrNull(),
                    notes = s.notes.ifBlank { null }
                )
            )
            _entryState.value = BodyEntryState(isSaved = true)
            onSaved()
        }
    }
}
