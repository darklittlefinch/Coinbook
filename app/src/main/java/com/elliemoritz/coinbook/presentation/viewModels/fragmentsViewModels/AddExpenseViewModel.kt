package com.elliemoritz.coinbook.presentation.viewModels.fragmentsViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elliemoritz.coinbook.domain.entities.operations.Expense
import com.elliemoritz.coinbook.domain.exceptions.EmptyFieldsException
import com.elliemoritz.coinbook.domain.exceptions.IncorrectNumberException
import com.elliemoritz.coinbook.domain.exceptions.NoChangesException
import com.elliemoritz.coinbook.domain.useCases.categoriesUseCases.GetCategoriesListUseCase
import com.elliemoritz.coinbook.domain.useCases.operationsUseCases.AddOperationUseCase
import com.elliemoritz.coinbook.domain.useCases.operationsUseCases.EditOperationUseCase
import com.elliemoritz.coinbook.domain.useCases.operationsUseCases.GetExpenseUseCase
import com.elliemoritz.coinbook.domain.useCases.userPreferencesUseCases.AddToBalanceUseCase
import com.elliemoritz.coinbook.domain.useCases.userPreferencesUseCases.RemoveFromBalanceUseCase
import com.elliemoritz.coinbook.presentation.states.fragmentsStates.FragmentExpenseState
import com.elliemoritz.coinbook.presentation.util.checkEmptyFields
import com.elliemoritz.coinbook.presentation.util.checkIncorrectNumbers
import com.elliemoritz.coinbook.presentation.util.checkNoChanges
import com.elliemoritz.coinbook.presentation.util.getCurrentTimeMillis
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
    private val categoriesStateFlow = categoriesFlow.map { categoriesList ->
            FragmentExpenseState.Categories(categoriesList.map { category ->
                    category.name
                })
        }

    private val _state = MutableSharedFlow<FragmentExpenseState>()

    val state: Flow<FragmentExpenseState>
        get() = _state.mergeWith(categoriesStateFlow)

    fun setData(id: Int) {
        viewModelScope.launch {
            val expense = getExpenseUseCase(id).first()
            _state.emit(FragmentExpenseState.Amount(expense.amount.toString()))

            val categoryPosition = getCategoryPosition(expense.expCategoryName)
            _state.emit(FragmentExpenseState.CategoryPosition(categoryPosition))
        }
    }

    fun createExpense(amountString: String, categoryName: String) {

        viewModelScope.launch {

            try {
                checkEmptyFields(amountString, categoryName)
                checkIncorrectNumbers(amountString)

                val amount = amountString.toInt()
                val expense = Expense(getCurrentTimeMillis(), amount, categoryName)
                addOperationUseCase(expense)
                removeFromBalanceUseCase(amount)

                setFinishState()

            } catch (e: EmptyFieldsException) {
                setEmptyFieldsState()
            } catch (e: IncorrectNumberException) {
                setIncorrectNumberState()
            }
        }
    }

    fun editExpense(newAmountString: String, newCategoryName: String, expenseId: Int) {

        viewModelScope.launch {

            try {
                checkEmptyFields(newAmountString, newCategoryName)
                checkIncorrectNumbers(newAmountString)

                val oldData = getExpenseUseCase(expenseId).first()
                val newAmount = newAmountString.toInt()

                checkNoChanges(
                    listOf(newAmount, newCategoryName),
                    listOf(oldData.amount, oldData.expCategoryName)
                )

                val expense = Expense(
                    oldData.dateTimeMillis, newAmount, newCategoryName, expenseId
                )
                editOperationUseCase(expense)

                val oldAmount = oldData.amount
                editBalance(oldAmount, newAmount)

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

    private suspend fun getCategoryPosition(categoryName: String): Int {
        val categories = categoriesFlow.first()
        var categoryPosition = 0
        for ((index, category) in categories.withIndex()) {
            if (category.name == categoryName) {
                categoryPosition = index
                break
            }
        }
        return categoryPosition
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