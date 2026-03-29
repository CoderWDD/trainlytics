package com.csd.trainlytics.ui.insights

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csd.trainlytics.domain.model.WeightTrendPoint
import com.csd.trainlytics.domain.model.MultiWeekComparison
import com.csd.trainlytics.domain.repository.BodyRepository
import com.csd.trainlytics.domain.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject

data class InsightsUiState(
    val weightTrend: List<WeightTrendPoint> = emptyList(),
    val multiWeekComparison: MultiWeekComparison? = null,
    val isLoading: Boolean = true
)

@HiltViewModel
class InsightsViewModel @Inject constructor(
    bodyRepository: BodyRepository,
    settingsRepository: SettingsRepository
) : ViewModel() {

    private val currentWeekStart = LocalDate.now()
        .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))

    val uiState: StateFlow<InsightsUiState> = combine(
        bodyRepository.getWeightTrend(90),
        settingsRepository.getMultiWeekComparison(currentWeekStart)
    ) { trend, comparison ->
        InsightsUiState(
            weightTrend = trend,
            multiWeekComparison = comparison,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = InsightsUiState()
    )
}
