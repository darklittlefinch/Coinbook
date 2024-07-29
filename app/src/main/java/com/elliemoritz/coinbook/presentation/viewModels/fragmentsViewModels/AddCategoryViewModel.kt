package com.elliemoritz.coinbook.presentation.viewModels.fragmentsViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elliemoritz.coinbook.domain.entities.Category
import com.elliemoritz.coinbook.domain.entities.Limit
import com.elliemoritz.coinbook.domain.exceptions.EmptyFieldsException
import com.elliemoritz.coinbook.domain.exceptions.IncorrectNumberException
import com.elliemoritz.coinbook.domain.exceptions.NoChangesException
import com.elliemoritz.coinbook.domain.useCases.categoriesUseCases.AddCategoryUseCase
import com.elliemoritz.coinbook.domain.useCases.categoriesUseCases.EditCategoryUseCase
import com.elliemoritz.coinbook.domain.useCases.categoriesUseCases.GetCategoryUseCase
import com.elliemoritz.coinbook.domain.useCases.limitsUseCases.AddLimitUseCase
import com.elliemoritz.coinbook.domain.useCases.limitsUseCases.EditLimitUseCase
import com.elliemoritz.coinbook.domain.useCases.limitsUseCases.GetLimitByCategoryIdUseCase
import com.elliemoritz.coinbook.domain.useCases.limitsUseCases.RemoveLimitUseCase
import com.elliemoritz.coinbook.presentation.states.fragmentsStates.FragmentCategoryState
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

class AddCategoryViewModel @Inject constructor(
    private val getCategoryUseCase: GetCategoryUseCase,
    private val addCategoryUseCase: AddCategoryUseCase,
    private val editCategoryUseCase: EditCategoryUseCase,
    private val getLimitByCategoryIdUseCase: GetLimitByCategoryIdUseCase,
    private val addLimitUseCase: AddLimitUseCase,
    private val editLimitUseCase: EditLimitUseCase,
    private val removeLimitUseCase: RemoveLimitUseCase
) : ViewModel() {

    private val categoryDataFlow = MutableSharedFlow<Category>()
    private val limitDataFlow = MutableSharedFlow<Limit?>()

    private val nameStateFlow = categoryDataFlow
        .map { FragmentCategoryState.Name(it.name) }

    private val limitStateFlow = limitDataFlow
        .map {
            val limitAmount = it?.amount ?: 0
            FragmentCategoryState.Limit(limitAmount.toString())
        }

    private val _state = MutableSharedFlow<FragmentCategoryState>()

    val state: Flow<FragmentCategoryState>
        get() = _state
            .mergeWith(nameStateFlow)
            .mergeWith(limitStateFlow)

    fun setData(id: Int) {
        viewModelScope.launch {
            val categoryData = getCategoryUseCase(id).first()
            categoryDataFlow.emit(categoryData)
            val limitData = getLimitByCategoryIdUseCase(categoryData.id).first()
            limitDataFlow.emit(limitData)
        }
    }

    fun createCategory(name: String, limitAmountString: String) {

        viewModelScope.launch {

            try {
                checkEmptyFields(name, limitAmountString)
                checkIncorrectNumbers(limitAmountString)

                val limitAmount = limitAmountString.toInt()
                val category = Category(name)
                addCategoryUseCase(category)

                if (limitAmount != 0) {
                    createLimit(limitAmount, category.id)
                }

                setFinishState()

            } catch (e: EmptyFieldsException) {
                setEmptyFieldsState()
            } catch (e: IncorrectNumberException) {
                setIncorrectNumberState()
            }
        }
    }

    fun editCategory(newName: String, newLimitAmountString: String) {

        viewModelScope.launch {

            try {
                checkEmptyFields(newName, newLimitAmountString)
                checkIncorrectNumbers(newLimitAmountString)

                val oldData = categoryDataFlow.first()
                val oldLimit = limitDataFlow.first()
                val oldLimitAmount = oldLimit?.amount ?: 0
                val newLimitAmount = newLimitAmountString.toInt()

                checkNoChanges(
                    listOf(newName, newLimitAmount),
                    listOf(oldData.name, oldLimitAmount)
                )

                val category = Category(newName, oldData.id)
                editCategoryUseCase(category)
                handleLimit(oldLimit, newLimitAmount, oldData.id)

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

    private suspend fun handleLimit(oldLimit: Limit?, newAmount: Int, categoryId: Int) {

        val oldLimitAmount = oldLimit?.amount ?: 0

        if (oldLimitAmount == 0 && newAmount != 0) {
            createLimit(newAmount, categoryId)
        } else if (oldLimitAmount != 0 && newAmount == 0) {
            oldLimit?.let { removeLimitUseCase(it) }
        } else if (oldLimitAmount != newAmount) {
            oldLimit?.let { editLimit(it, newAmount) }
        }
    }

    private suspend fun createLimit(amount: Int, categoryId: Int) {
        val limit = Limit(amount, categoryId)
        addLimitUseCase(limit)
    }

    private suspend fun editLimit(limit: Limit, newAmount: Int) {
        limit.amount = newAmount
        editLimitUseCase(limit)
    }

    private suspend fun setFinishState() {
        _state.emit(FragmentCategoryState.Finish)
    }

    private suspend fun setEmptyFieldsState() {
        _state.emit(FragmentCategoryState.EmptyFields)
    }

    private suspend fun setNoChangesState() {
        _state.emit(FragmentCategoryState.NoChanges)
    }

    private suspend fun setIncorrectNumberState() {
        _state.emit(FragmentCategoryState.IncorrectNumber)
    }
}