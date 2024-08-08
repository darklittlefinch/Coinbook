package com.elliemoritz.coinbook.presentation.viewModels.fragmentsViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elliemoritz.coinbook.domain.entities.operations.Expense
import com.elliemoritz.coinbook.domain.exceptions.EmptyFieldsException
import com.elliemoritz.coinbook.domain.exceptions.IncorrectNumberException
import com.elliemoritz.coinbook.domain.exceptions.NoChangesException
import com.elliemoritz.coinbook.domain.exceptions.NotEnoughMoneyException
import com.elliemoritz.coinbook.domain.useCases.categoriesUseCases.GetCategoriesListUseCase
import com.elliemoritz.coinbook.domain.useCases.categoriesUseCases.GetCategoryByNameUseCase
import com.elliemoritz.coinbook.domain.useCases.expensesUseCases.AddExpenseUseCase
import com.elliemoritz.coinbook.domain.useCases.expensesUseCases.EditExpenseUseCase
import com.elliemoritz.coinbook.domain.useCases.expensesUseCases.GetExpenseUseCase
import com.elliemoritz.coinbook.domain.useCases.userPreferencesUseCases.AddToBalanceUseCase
import com.elliemoritz.coinbook.domain.useCases.userPreferencesUseCases.GetBalanceUseCase
import com.elliemoritz.coinbook.domain.useCases.userPreferencesUseCases.RemoveFromBalanceUseCase
import com.elliemoritz.coinbook.presentation.states.fragmentsStates.FragmentExpenseState
import com.elliemoritz.coinbook.presentation.util.checkEmptyFields
import com.elliemoritz.coinbook.presentation.util.checkIncorrectNumbers
import com.elliemoritz.coinbook.presentation.util.checkNoChanges
import com.elliemoritz.coinbook.presentation.util.checkNotEnoughMoney
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
    private val addExpenseUseCase: AddExpenseUseCase,
    private val editExpenseUseCase: EditExpenseUseCase,
    getCategoriesListUseCase: GetCategoriesListUseCase,
    private val getCategoryByNameUseCase: GetCategoryByNameUseCase,
    private val getBalanceUseCase: GetBalanceUseCase,
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

            val categoryPosition = getCategoryPosition(expense.categoryName)
            _state.emit(FragmentExpenseState.CategoryPosition(categoryPosition))
        }
    }

    fun createExpense(amountString: String, categoryName: String) {

        viewModelScope.launch {

            try {
                checkEmptyFields(amountString, categoryName)
                checkIncorrectNumbers(amountString)

                val amount = amountString.toInt()
                val balance = getBalanceUseCase().first()

                checkNotEnoughMoney(amount, balance)

                val category = getCategoryByNameUseCase(categoryName).first()
                    ?: throw RuntimeException("User selected a non-existent category (how?!)")

                val expense = Expense(
                    amount = amount,
                    categoryId = category.id,
                    categoryName = categoryName,
                    dateTimeMillis = getCurrentTimeMillis(),
                )

                addExpenseUseCase(expense)
                removeFromBalanceUseCase(amount)

                setFinishState()

            } catch (e: EmptyFieldsException) {
                setEmptyFieldsState()
            } catch (e: IncorrectNumberException) {
                setIncorrectNumberState()
            } catch (e: NotEnoughMoneyException) {
                setNotEnoughMoneyState()
            }
        }
    }

    fun editExpense(newAmountString: String, newCategoryName: String, id: Int) {

        viewModelScope.launch {

            try {
                checkEmptyFields(newAmountString, newCategoryName)
                checkIncorrectNumbers(newAmountString)

                val oldData = getExpenseUseCase(id).first()
                val newAmount = newAmountString.toInt()
                val balance = getBalanceUseCase().first()

                if (newAmount > oldData.amount) {
                    val difference = newAmount - oldData.amount
                    checkNotEnoughMoney(difference, balance)
                }

                checkNoChanges(
                    listOf(newAmount, newCategoryName),
                    listOf(oldData.amount, oldData.categoryId)
                )

                val category = getCategoryByNameUseCase(newCategoryName).first()
                    ?: throw RuntimeException("User selected a non-existent category (how?!)")

                val expense = Expense(
                    amount = newAmount,
                    categoryId = category.id,
                    categoryName = newCategoryName,
                    dateTimeMillis = oldData.dateTimeMillis,
                    id = id
                )
                editExpenseUseCase(expense)

                val oldAmount = oldData.amount
                editBalance(oldAmount, newAmount)

                setFinishState()

            } catch (e: EmptyFieldsException) {
                setEmptyFieldsState()
            } catch (e: IncorrectNumberException) {
                setIncorrectNumberState()
            } catch (e: NoChangesException) {
                setNoChangesState()
            } catch (e: NotEnoughMoneyException) {
                setNotEnoughMoneyState()
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

    private suspend fun setNotEnoughMoneyState() {
        _state.emit(FragmentExpenseState.NotEnoughMoney)
    }
}