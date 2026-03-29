package com.csd.trainlytics.domain.usecase.settings

import com.csd.trainlytics.domain.model.TodaySummary
import com.csd.trainlytics.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class GetTodaySummaryUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    operator fun invoke(date: LocalDate = LocalDate.now()): Flow<TodaySummary> =
        settingsRepository.getTodaySummary(date)
}
