package com.csd.trainlytics.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csd.trainlytics.core.navigation.NavRoutes
import com.csd.trainlytics.domain.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class NavEntryViewModel @Inject constructor(
    settingsRepository: SettingsRepository
) : ViewModel() {

    /** null = still loading */
    val startDestination = settingsRepository.observeUserProfile()
        .map { profile ->
            if (profile.onboardingCompleted) NavRoutes.Today.route
            else NavRoutes.Onboarding.route
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )
}
