package com.csd.trainlytics.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csd.trainlytics.domain.model.MuscleGroup
import com.csd.trainlytics.domain.model.PersonalRecord
import com.csd.trainlytics.domain.repository.WorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import javax.inject.Inject

data class PersonalRecordsUiState(
    val records: List<PersonalRecord> = emptyList(),
    val oneRMHistory: Map<Long, List<Float>> = emptyMap(),
    val totalPRCount: Int = 0,
    val thisMonthNewPRs: Int = 0
)

@HiltViewModel
class PersonalRecordsViewModel @Inject constructor(
    private val workoutRepository: WorkoutRepository
) : ViewModel() {

    val searchQuery = MutableStateFlow("")
    val selectedMuscleGroup = MutableStateFlow(MuscleGroup.ALL)

    private val allRecords: StateFlow<List<PersonalRecord>> = workoutRepository
        .getPersonalRecords()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val filteredRecords: StateFlow<List<PersonalRecord>> = combine(
        allRecords, searchQuery, selectedMuscleGroup
    ) { records, query, group ->
        records.filter { record ->
            val matchesGroup = group == MuscleGroup.ALL || record.muscleGroup == group
            val matchesQuery = query.isBlank() || record.exerciseName.contains(query, ignoreCase = true)
            matchesGroup && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val headerStats: StateFlow<Pair<Int, Int>> = allRecords.map { records ->
        val total = records.size
        val thisMonth = records.count { it.date.month == LocalDate.now().month && it.date.year == LocalDate.now().year }
        total to thisMonth
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0 to 0)

    @OptIn(ExperimentalCoroutinesApi::class)
    val oneRMHistoryMap: StateFlow<Map<Long, List<Float>>> = filteredRecords.flatMapLatest { records ->
        if (records.isEmpty()) {
            kotlinx.coroutines.flow.flowOf(emptyMap())
        } else {
            val historyFlows = records.map { record ->
                workoutRepository.getOneRMHistoryForExercise(record.exerciseId)
                    .map { history -> record.exerciseId to history }
            }
            combine(historyFlows) { pairs -> pairs.toMap() }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyMap())

    fun onSearchQueryChange(query: String) { searchQuery.value = query }
    fun onMuscleGroupSelected(group: MuscleGroup) { selectedMuscleGroup.value = group }
}
