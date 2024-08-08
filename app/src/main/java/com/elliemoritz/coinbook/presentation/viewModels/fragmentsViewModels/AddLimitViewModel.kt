package com.elliemoritz.coinbook.presentation.viewModels.fragmentsViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elliemoritz.coinbook.domain.entities.Limit
import com.elliemoritz.coinbook.domain.exceptions.EmptyFieldsException
import com.elliemoritz.coinbook.domain.exceptions.IncorrectNumberException
import com.elliemoritz.coinbook.domain.exceptions.LimitWithoutValueException
import com.elliemoritz.coinbook.domain.exceptions.NoChangesException
import com.elliemoritz.coinbook.domain.useCases.categoriesUseCases.GetCategoriesListUseCase
import com.elliemoritz.coinbook.domain.useCases.categoriesUseCases.GetCategoryByNameUseCase
import com.elliemoritz.coinbook.domain.useCases.categoriesUseCases.GetCategoryUseCase
import com.elliemoritz.coinbook.domain.useCases.expensesUseCases.GetTotalExpensesAmountByCategoryForMonthUseCase
import com.elliemoritz.coinbook.domain.useCases.limitsUseCases.AddLimitUseCase
import com.elliemoritz.coinbook.domain.useCases.limitsUseCases.EditLimitUseCase
import com.elliemoritz.coinbook.domain.useCases.limitsUseCases.GetLimitByCategoryIdUseCase
import com.elliemoritz.coinbook.domain.useCases.limitsUseCases.GetLimitUseCase
import com.elliemoritz.coinbook.domain.useCases.userPreferencesUseCases.GetCurrencyUseCase
import com.elliemoritz.coinbook.presentation.states.fragmentsStates.FragmentLimitState
import com.elliemoritz.coinbook.presentation.util.checkEmptyFields
import com.elliemoritz.coinbook.presentation.util.checkIncorrectNumbers
import com.elliemoritz.coinbook.presentation.util.checkLimitWithoutValue
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
    private val getCategoryByNameUseCase: GetCategoryByNameUseCase,
    private val getTotalExpensesAmountUseCase: GetTotalExpensesAmountByCategoryForMonthUseCase,
    private val getCurrencyUseCase: GetCurrencyUseCase
) : ViewModel() {

    private val categoriesListFlow = getCategoriesListUseCase()
    private val categoriesStateFlow = categoriesListFlow
        .map { categoriesList ->
            FragmentLimitState.Categories(categoriesList
                .map { category ->
                    category.name
                })
        }

    private val _state = MutableSharedFlow<FragmentLimitState>()

    val state: Flow<FragmentLimitState>
        get() = _state
            .mergeWith(categoriesStateFlow)

    fun setData(id: Long) {

        viewModelScope.launch {

            val limit = getLimitUseCase(id).first()
            _state.emit(
                FragmentLimitState.Amount(limit.limitAmount.toString())
            )

            val category = getCategoryUseCase(limit.categoryId).first()
            val categoryPosition = getCategoryPosition(category.name)
            _state.emit(
                FragmentLimitState.CategoryPosition(categoryPosition)
            )
        }
    }

    fun createLimit(limitAmountString: String, categoryName: String) {

        viewModelScope.launch {

            try {
                val category = getCategoryByNameUseCase(categoryName).first()
                    ?: throw RuntimeException("User selected a non-existent category (how?!)")

                checkEmptyFields(limitAmountString, categoryName)
                checkIncorrectNumbers(limitAmountString)

                val limitAmount = limitAmountString.toInt()

                checkLimitWithoutValue(limitAmount)

                val possibleLimit = getLimitByCategoryIdUseCase(category.id).first()

                if (possibleLimit == null) {
                    val realAmount = getTotalExpensesAmountUseCase(category.id).first()
                    val currency = getCurrencyUseCase().first()

                    val limit = Limit(
                        limitAmount = limitAmount,
                        realAmount = realAmount,
                        categoryId = category.id,
                        categoryName = category.name,
                        currency = currency
                    )
                    addLimitUseCase(limit)
                } else {
                    val limit = possibleLimit.copy(limitAmount = limitAmount)
                    editLimitUseCase(limit)
                }

                setFinishState()

            } catch (e: EmptyFieldsException) {
                setEmptyFieldsState()
            } catch (e: IncorrectNumberException) {
                setIncorrectNumberState()
            } catch (e: LimitWithoutValueException) {
                setLimitWithoutValueState()
            }
        }
    }

    fun editLimit(newLimitAmountString: String, newCategoryName: String, id: Long) {

        viewModelScope.launch {

            try {
                val newCategory = getCategoryByNameUseCase(newCategoryName).first()
                    ?: throw RuntimeException("User selected a non-existent category (how?!)")

                checkEmptyFields(newLimitAmountString, newCategoryName)
                checkIncorrectNumbers(newLimitAmountString)

                val oldData = getLimitUseCase(id).first()
                val newLimitAmount = newLimitAmountString.toInt()

                checkLimitWithoutValue(newLimitAmount)

                checkNoChanges(
                    listOf(newLimitAmount, newCategory.id),
                    listOf(oldData.limitAmount, oldData.categoryId)
                )

                val limit = oldData.copy(
                    limitAmount = newLimitAmount,
                    categoryId = newCategory.id,
                    categoryName = newCategory.name,
                    id = id
                )
                editLimitUseCase(limit)

                setFinishState()

            } catch (e: EmptyFieldsException) {
                setEmptyFieldsState()
            } catch (e: IncorrectNumberException) {
                setIncorrectNumberState()
            } catch (e: NoChangesException) {
                setNoChangesState()
            } catch (e: LimitWithoutValueException) {
                setLimitWithoutValueState()
            }
        }
    }

    private suspend fun getCategoryPosition(categoryName: String): Int {
        val categoriesList = categoriesListFlow.first()
        var categoryPosition = 0
        for ((index, category) in categoriesList.withIndex()) {
            if (category.name == categoryName) {
                categoryPosition = index
                break
            }
        }
        return categoryPosition
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

    private suspend fun setLimitWithoutValueState() {
        _state.emit(FragmentLimitState.LimitWithoutValue)
    }
}