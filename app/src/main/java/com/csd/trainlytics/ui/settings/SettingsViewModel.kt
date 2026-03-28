package com.csd.trainlytics.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csd.trainlytics.domain.model.UserProfile
import com.csd.trainlytics.domain.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val profile: UserProfile = UserProfile(),
    val isLoading: Boolean = true
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsUiState())
    val state: StateFlow<SettingsUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            settingsRepository.observeUserProfile().collect { profile ->
                _state.update { it.copy(profile = profile, isLoading = false) }
            }
        }
    }

    fun updateProfile(profile: UserProfile) {
        viewModelScope.launch {
            settingsRepository.updateUserProfile(profile)
        }
    }
}
