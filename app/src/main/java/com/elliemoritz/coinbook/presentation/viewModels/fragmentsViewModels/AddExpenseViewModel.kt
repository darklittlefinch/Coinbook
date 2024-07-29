package com.elliemoritz.coinbook.presentation.viewModels.fragmentsViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elliemoritz.coinbook.domain.entities.operations.Expense
import com.elliemoritz.coinbook.domain.useCases.categoriesUseCases.GetCategoriesListUseCase
import com.elliemoritz.coinbook.domain.useCases.operationsUseCases.AddOperationUseCase
import com.elliemoritz.coinbook.domain.useCases.operationsUseCases.EditOperationUseCase
import com.elliemoritz.coinbook.domain.useCases.operationsUseCases.GetExpenseUseCase
import com.elliemoritz.coinbook.domain.useCases.userPreferencesUseCases.AddToBalanceUseCase
import com.elliemoritz.coinbook.domain.useCases.userPreferencesUseCases.RemoveFromBalanceUseCase
import com.elliemoritz.coinbook.presentation.states.fragmentsStates.FragmentExpenseState
import com.elliemoritz.coinbook.presentation.util.getCurrentTimestamp
import com.elliemoritz.coinbook.presentation.util.mergeWith
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.abs

class AddExpenseViewModel @Inject constructor(
    private val getExpenseUseCase: GetExpenseUseCase,
    private val addOperationUseCase: AddOperationUseCase,
    private val editOperationUseCase: EditOperationUseCase,
    getCategoriesListUseCase: GetCategoriesListUseCase,
    private val addToBalanceUseCase: AddToBalanceUseCase,
    private val removeFromBalanceUseCase: RemoveFromBalanceUseCase
) : ViewModel() {

    private val categoriesFlow = getCategoriesListUseCase()

    private val categoriesStateFlow = categoriesFlow
        .map { categoriesList ->
            FragmentExpenseState.Categories(categoriesList
                .map { category ->
                    category.name
                })
        }

    private val dataFlow = MutableSharedFlow<Expense>()

    private val amountStateFlow = dataFlow
        .map { FragmentExpenseState.Amount(it.amount.toString()) }

    private val categoryPositionStateFlow = dataFlow
        .map {
            val currentCategoryName = it.expCategoryName
            val categories = categoriesFlow.first()
            var categoryPosition = 0
            for ((index, category) in categories.withIndex()) {
                if (category.name === currentCategoryName) {
                    categoryPosition = index
                    break
                }
            }
            FragmentExpenseState.CategoryPosition(categoryPosition)
        }

    private val _state = MutableSharedFlow<FragmentExpenseState>()

    val state: Flow<FragmentExpenseState>
        get() = _state
            .mergeWith(categoriesStateFlow)
            .mergeWith(amountStateFlow)
            .mergeWith(categoryPositionStateFlow)

    fun setData(id: Int) {
        viewModelScope.launch {
            val data = getExpenseUseCase(id).first()
            dataFlow.emit(data)
        }
    }

    fun createExpense(amountString: String, categoryName: String) {
        viewModelScope.launch {

            if (amountString.isEmpty()) {
                setEmptyFieldsState()
                return@launch
            }

            try {
                val amount = amountString.toInt()
                val expense = Expense(getCurrentTimestamp(), amount, categoryName)
                addOperationUseCase(expense)
                removeFromBalanceUseCase(amount)
                setFinishState()
            } catch (e: NumberFormatException) {
                setIncorrectNumberState()
            }
        }
    }

    fun editExpense(newAmountString: String, categoryName: String) {
        viewModelScope.launch {

            if (newAmountString.isEmpty()) {
                setEmptyFieldsState()
                return@launch
            }

            try {
                val oldData = dataFlow.first()
                val newAmount = newAmountString.toInt()

                if (newAmount == oldData.amount && categoryName == oldData.expCategoryName) {
                    setNoChangesState()
                    return@launch
                }

                val expense = Expense(oldData.date, newAmount, categoryName, oldData.id)
                editOperationUseCase(expense)

                val oldAmount = oldData.amount
                editBalance(oldAmount, newAmount)

                setFinishState()
            } catch (e: NumberFormatException) {
                setIncorrectNumberState()
            }
        }
    }

    private suspend fun editBalance(oldAmount: Int, newAmount: Int) {
        val difference = abs(newAmount - oldAmount)
        if (newAmount > oldAmount) {
            removeFromBalanceUseCase(difference)
        } else if (newAmount < oldAmount) {
            addToBalanceUseCase(difference)
        }
    }

    private suspend fun setFinishState() {
        _state.emit(FragmentExpenseState.Finish)
    }

    private suspend fun setEmptyFieldsState() {
        _state.emit(FragmentExpenseState.EmptyFields)
    }

    private suspend fun setNoChangesState() {
        _state.emit(FragmentExpenseState.NoChanges)
    }

    private suspend fun setIncorrectNumberState() {
        _state.emit(FragmentExpenseState.IncorrectNumber)
    }
}