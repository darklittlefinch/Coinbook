package com.elliemoritz.coinbook.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elliemoritz.coinbook.domain.entities.Limit
import com.elliemoritz.coinbook.domain.useCases.categoriesUseCases.GetCategoriesListUseCase
import com.elliemoritz.coinbook.domain.useCases.limitsUseCases.GetLimitsListUseCase
import com.elliemoritz.coinbook.domain.useCases.limitsUseCases.RemoveLimitUseCase
import com.elliemoritz.coinbook.presentation.states.LimitsState
import com.elliemoritz.coinbook.presentation.util.mergeWith
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class LimitsViewModel @Inject constructor(
    getLimitsListUseCase: GetLimitsListUseCase,
    private val removeLimitUseCase: RemoveLimitUseCase,
    private val getCategoriesListUseCase: GetCategoriesListUseCase
) : ViewModel() {

    private val limitsListFlow = getLimitsListUseCase()
    private val limitsListStateFlow = limitsListFlow
        .map { LimitsState.LimitsList(it) }
    private val hasDataStateFlow = limitsListFlow
        .map {
            if (it.isEmpty()) {
                LimitsState.NoData
            } else {
                LimitsState.HasData
            }
        }

    private val _state = MutableSharedFlow<LimitsState>()

    val state: Flow<LimitsState>
        get() = _state
            .mergeWith(limitsListStateFlow)
            .mergeWith(hasDataStateFlow)

    fun removeLimit(limit: Limit) {
        viewModelScope.launch {
            removeLimitUseCase(limit)
        }
    }

    fun checkCategories() {
        viewModelScope.launch {
            val categories = getCategoriesListUseCase().first()
            if (categories.isEmpty()) {
                _state.emit(LimitsState.NoCategoriesError)
            } else {
                _state.emit(LimitsState.PermitAddLimit)
            }
        }
    }
}