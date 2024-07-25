package com.elliemoritz.coinbook.presentation.viewModels.fragmentsViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elliemoritz.coinbook.domain.entities.operations.Expense
import com.elliemoritz.coinbook.domain.useCases.categoriesUseCases.GetCategoriesListUseCase
import com.elliemoritz.coinbook.domain.useCases.operationsUseCases.AddOperationUseCase
import com.elliemoritz.coinbook.domain.useCases.operationsUseCases.EditOperationUseCase
import com.elliemoritz.coinbook.domain.useCases.operationsUseCases.GetOperationUseCase
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
    private val getOperationUseCase: GetOperationUseCase,
    private val addOperationUseCase: AddOperationUseCase,
    private val editOperationUseCase: EditOperationUseCase,
    getCategoriesListUseCase: GetCategoriesListUseCase,
    private val addToBalanceUseCase: AddToBalanceUseCase,
    private val removeFromBalanceUseCase: RemoveFromBalanceUseCase
) : ViewModel() {

    private val dataFlow = MutableSharedFlow<Expense>()

    private val dataStateFlow = dataFlow
        .map {
            FragmentExpenseState.Data(
                it.amount.toString(),
                it.expCategoryName
            )
        }

    private val categoriesStateFlow = getCategoriesListUseCase()
        .map { categoriesList ->
            FragmentExpenseState.Categories(categoriesList
                .map { category ->
                    category.name
                })
        }

    private val _state = MutableSharedFlow<FragmentExpenseState>()

    val state: Flow<FragmentExpenseState>
        get() = _state
            .mergeWith(categoriesStateFlow)
            .mergeWith(dataStateFlow)

    fun setData(id: Int) {
        viewModelScope.launch {
            val data = getOperationUseCase(id).first() as Expense
            dataFlow.emit(data)
        }
    }

    fun addExpense(amountString: String, categoryName: String) {
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

    fun editExpense(id: Int, newAmountString: String, categoryName: String) {
        viewModelScope.launch {

            if (newAmountString.isEmpty()) {
                setEmptyFieldsState()
                return@launch
            }

            try {
                val newAmount = newAmountString.toInt()
                val expense = Expense(getCurrentTimestamp(), newAmount, categoryName, id)
                editOperationUseCase(expense)

                val oldAmount = dataFlow.first().amount
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

    private suspend fun setIncorrectNumberState() {
        _state.emit(FragmentExpenseState.IncorrectNumber)
    }
}