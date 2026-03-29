package com.csd.trainlytics.domain.usecase.settings

import com.csd.trainlytics.domain.model.WeeklyReviewData
import com.csd.trainlytics.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class GetWeeklyReviewUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    operator fun invoke(weekStart: LocalDate): Flow<WeeklyReviewData> =
        settingsRepository.getWeeklyReview(weekStart)
}
