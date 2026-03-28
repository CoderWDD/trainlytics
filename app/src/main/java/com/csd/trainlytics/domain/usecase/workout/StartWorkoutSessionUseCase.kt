package com.csd.trainlytics.domain.usecase.workout

import com.csd.trainlytics.domain.repository.WorkoutRepository
import javax.inject.Inject

class StartWorkoutSessionUseCase @Inject constructor(
    private val workoutRepository: WorkoutRepository,
    private val templateRepository: com.csd.trainlytics.domain.repository.TemplateRepository
) {
    suspend operator fun invoke(templateId: Long? = null): Long {
        if (templateId != null) {
            val template = templateRepository.getWorkoutTemplateById(templateId)
                ?: error("Template $templateId not found")
            templateRepository.incrementWorkoutTemplateUsage(templateId)
            return workoutRepository.startSession(templateId)
        }
        return workoutRepository.startSession()
    }
}
