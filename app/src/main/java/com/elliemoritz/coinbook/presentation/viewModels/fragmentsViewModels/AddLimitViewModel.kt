package com.elliemoritz.coinbook.presentation.viewModels.fragmentsViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elliemoritz.coinbook.domain.entities.Limit
import com.elliemoritz.coinbook.domain.exceptions.EmptyFieldsException
import com.elliemoritz.coinbook.domain.exceptions.IncorrectNumberException
import com.elliemoritz.coinbook.domain.exceptions.NoChangesException
import com.elliemoritz.coinbook.domain.useCases.categoriesUseCases.GetCategoriesListUseCase
import com.elliemoritz.coinbook.domain.useCases.categoriesUseCases.GetCategoryByNameUseCase
import com.elliemoritz.coinbook.domain.useCases.categoriesUseCases.GetCategoryUseCase
import com.elliemoritz.coinbook.domain.useCases.limitsUseCases.AddLimitUseCase
import com.elliemoritz.coinbook.domain.useCases.limitsUseCases.EditLimitUseCase
import com.elliemoritz.coinbook.domain.useCases.limitsUseCases.GetLimitByCategoryIdUseCase
import com.elliemoritz.coinbook.domain.useCases.limitsUseCases.GetLimitUseCase
import com.elliemoritz.coinbook.presentation.states.fragmentsStates.FragmentLimitState
import com.elliemoritz.coinbook.presentation.util.checkEmptyFields
import com.elliemoritz.coinbook.presentation.util.checkIncorrectNumbers
import com.elliemoritz.coinbook.presentation.util.checkNoChanges
import com.elliemoritz.coinbook.presentation.util.mergeWith
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddLimitViewModel @Inject constructor(
    private val getLimitUseCase: GetLimitUseCase,
    private val getLimitByCategoryIdUseCase: GetLimitByCategoryIdUseCase,
    private val addLimitUseCase: AddLimitUseCase,
    private val editLimitUseCase: EditLimitUseCase,
    getCategoriesListUseCase: GetCategoriesListUseCase,
    private val getCategoryUseCase: GetCategoryUseCase,
    private val getCategoryByNameUseCase: GetCategoryByNameUseCase
) : ViewModel() {

    private val categoriesListFlow = getCategoriesListUseCase()

    private val categoriesStateFlow = categoriesListFlow
        .map { categoriesList ->
            FragmentLimitState.Categories(categoriesList
                .map { category ->
                    category.name
                })
        }

    private val dataFlow = MutableSharedFlow<Limit>()

    private val amountStateFlow = dataFlow
        .map { FragmentLimitState.Amount(it.amount.toString()) }

    private val categoryPositionStateFlow = dataFlow
        .map {
            val currentCategory = getCategoryUseCase(it.categoryId).first()
            val categoriesList = categoriesListFlow.first()
            var categoryPosition = 0
            for ((index, category) in categoriesList.withIndex()) {
                if (category.name == currentCategory.name) {
                    categoryPosition = index
                    break
                }
            }
            FragmentLimitState.CategoryPosition(categoryPosition)
        }

    private val _state = MutableSharedFlow<FragmentLimitState>()

    val state: Flow<FragmentLimitState>
        get() = _state
            .mergeWith(categoriesStateFlow)
            .mergeWith(amountStateFlow)
            .mergeWith(categoryPositionStateFlow)

    fun setData(id: Int) {
        viewModelScope.launch {
            val limit = getLimitUseCase(id).first()
            dataFlow.emit(limit)
        }
    }

    fun createLimit(amountString: String, categoryName: String) {

        viewModelScope.launch {

            try {
                val category = getCategoryByNameUseCase(categoryName).first()

                checkEmptyFields(amountString, categoryName)
                checkIncorrectNumbers(amountString)

                if (category == null) {
                    throw RuntimeException("Somehow user selected a non-existent category...")
                }

                val amount = amountString.toInt()
                val possibleLimit = getLimitByCategoryIdUseCase(category.id).first()

                if (possibleLimit == null) {
                    val limit = Limit(amount, category.id)
                    addLimitUseCase(limit)
                } else {
                    val limit = possibleLimit.copy(amount = amount)
                    editLimitUseCase(limit)
                }

                setFinishState()

            } catch (e: EmptyFieldsException) {
                setEmptyFieldsState()
            } catch (e: IncorrectNumberException) {
                setIncorrectNumberState()
            }
        }
    }

    fun editLimit(newAmountString: String, newCategoryName: String) {

        viewModelScope.launch {

            try {
                val newCategory = getCategoryByNameUseCase(newCategoryName).first()

                checkEmptyFields(newAmountString, newCategoryName)
                checkIncorrectNumbers(newAmountString)

                if (newCategory == null) {
                    throw RuntimeException("Somehow user selected a non-existent category...")
                }

                val oldData = dataFlow.first()
                val newAmount = newAmountString.toInt()

                checkNoChanges(
                    listOf(newAmount, newCategory.id),
                    listOf(oldData.amount, oldData.categoryId)
                )

                val limit = Limit(newAmount, newCategory.id, oldData.id)
                editLimitUseCase(limit)

                setFinishState()

            } catch (e: EmptyFieldsException) {
                setEmptyFieldsState()
            } catch (e: IncorrectNumberException) {
                setIncorrectNumberState()
            } catch (e: NoChangesException) {
                setNoChangesState()
            }
        }
    }

    private suspend fun setFinishState() {
        _state.emit(FragmentLimitState.Finish)
    }

    private suspend fun setEmptyFieldsState() {
        _state.emit(FragmentLimitState.EmptyFields)
    }

    private suspend fun setNoChangesState() {
        _state.emit(FragmentLimitState.NoChanges)
    }

    private suspend fun setIncorrectNumberState() {
        _state.emit(FragmentLimitState.IncorrectNumber)
    }

}