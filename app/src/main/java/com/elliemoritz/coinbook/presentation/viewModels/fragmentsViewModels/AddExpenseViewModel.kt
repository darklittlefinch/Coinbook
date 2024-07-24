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
    private val getCategoriesListUseCase: GetCategoriesListUseCase,
    private val addToBalanceUseCase: AddToBalanceUseCase,
    private val removeFromBalanceUseCase: RemoveFromBalanceUseCase
) : ViewModel() {

    private val oldData = MutableSharedFlow<Expense>()

    private val dataFlow = oldData
        .map {
            FragmentExpenseState.Data(
                it.amount.toString(),
                it.expCategoryName
            )
        }

    private val categoriesFlow = getCategoriesListUseCase()
        .map { categoriesList ->
            FragmentExpenseState.Categories(categoriesList
                .map { category ->
                    category.name
                })
        }

    private val errorFlow = MutableSharedFlow<FragmentExpenseState>()
    private val finishFlow = MutableSharedFlow<FragmentExpenseState>()

    private val _state = MutableSharedFlow<FragmentExpenseState>()
        .mergeWith(categoriesFlow)
        .mergeWith(dataFlow)
        .mergeWith(finishFlow)
        .mergeWith(errorFlow)

    val state: Flow<FragmentExpenseState>
        get() = _state

    fun setData(id: Int) {
        viewModelScope.launch {
            val data = getOperationUseCase(id).first() as Expense
            oldData.emit(data)
        }
    }

    fun addExpense(amountString: String, categoryName: String) {
        viewModelScope.launch {

            if (amountString.isEmpty()) {
                setErrorState()
                return@launch
            }

            try {
                val amount = amountString.toInt()
                val expense = Expense(getCurrentTimestamp(), amount, categoryName)
                addOperationUseCase(expense)
                removeFromBalanceUseCase(amount)
                setFinishState()
            } catch (e: NumberFormatException) {
                setErrorState()
            }
        }
    }

    fun editExpense(id: Int, newAmountString: String, categoryName: String) {
        viewModelScope.launch {

            if (newAmountString.isEmpty()) {
                setErrorState()
                return@launch
            }

            try {
                val newAmount = newAmountString.toInt()
                val expense = Expense(getCurrentTimestamp(), newAmount, categoryName, id)
                editOperationUseCase(expense)

                val oldAmount = oldData.first().amount
                editBalance(oldAmount, newAmount)

                setFinishState()
            } catch (e: NumberFormatException) {
                setErrorState()
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
        finishFlow.emit(FragmentExpenseState.Finish)
    }

    private suspend fun setErrorState() {
        errorFlow.emit(FragmentExpenseState.Error)
    }
}