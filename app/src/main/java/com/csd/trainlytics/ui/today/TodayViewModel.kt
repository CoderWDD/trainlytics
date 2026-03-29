package com.csd.trainlytics.ui.today

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csd.trainlytics.domain.model.TodaySummary
import com.csd.trainlytics.domain.model.UserSettings
import com.csd.trainlytics.domain.usecase.settings.GetTodaySummaryUseCase
import com.csd.trainlytics.domain.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import javax.inject.Inject

data class TodayUiState(
    val summary: TodaySummary? = null,
    val settings: UserSettings = UserSettings(),
    val isLoading: Boolean = true
)

@HiltViewModel
class TodayViewModel @Inject constructor(
    getTodaySummary: GetTodaySummaryUseCase,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val uiState: StateFlow<TodayUiState> = combine(
        getTodaySummary(LocalDate.now()),
        settingsRepository.getUserSettings()
    ) { summary, settings ->
        TodayUiState(summary = summary, settings = settings, isLoading = false)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = TodayUiState()
    )
}
