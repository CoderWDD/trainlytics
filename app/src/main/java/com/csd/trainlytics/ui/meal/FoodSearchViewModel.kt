package com.csd.trainlytics.ui.meal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csd.trainlytics.domain.model.FoodCategory
import com.csd.trainlytics.domain.model.FoodItem
import com.csd.trainlytics.domain.repository.MealRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class FoodSearchUiState(
    val query: String = "",
    val selectedCategory: FoodCategory = FoodCategory.COMMON,
    val foodItems: List<FoodItem> = emptyList()
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class FoodSearchViewModel @Inject constructor(
    private val mealRepository: MealRepository
) : ViewModel() {

    private val _query = MutableStateFlow("")
    private val _selectedCategory = MutableStateFlow(FoodCategory.COMMON)

    val uiState: StateFlow<FoodSearchUiState> = combine(_query, _selectedCategory) { query, cat ->
        Pair(query, cat)
    }.flatMapLatest { (query, cat) ->
        if (query.isNotBlank()) {
            mealRepository.searchFoodItems(query)
        } else {
            mealRepository.getAllFoodItems()
        }
    }.combine(combine(_query, _selectedCategory) { q, c -> Pair(q, c) }) { items, (query, cat) ->
        val filtered = if (query.isBlank() && cat != FoodCategory.COMMON) {
            items.filter { it.category == cat }
        } else items
        FoodSearchUiState(query = query, selectedCategory = cat, foodItems = filtered)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = FoodSearchUiState()
    )

    fun updateQuery(query: String) { _query.value = query }
    fun selectCategory(cat: FoodCategory) { _selectedCategory.value = cat }
}
