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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
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

    private val _state = MutableSharedFlow<FragmentCategoryState>()

    val state: Flow<FragmentCategoryState>
        get() = _state

    fun setData(id: Long) {

        viewModelScope.launch {

            val categoryData = getCategoryUseCase(id).first()
            _state.emit(
                FragmentCategoryState.Name(categoryData.name)
            )

            val limitData = getLimitByCategoryIdUseCase(categoryData.id).first()
            val limitAmount = limitData?.limitAmount ?: 0
            _state.emit(
                FragmentCategoryState.Limit(limitAmount.toString())
            )
        }
    }

    fun createCategory(name: String, limitAmountString: String) {

        viewModelScope.launch {

            try {
                checkEmptyFields(name, limitAmountString)
                checkIncorrectNumbers(limitAmountString)

                val limitAmount = limitAmountString.toInt()
                val category = Category(name = name)
                val id = addCategoryUseCase(category)

                if (limitAmount != 0) {
                    createLimit(limitAmount, category.name, id)
                }

                setFinishState()

            } catch (e: EmptyFieldsException) {
                setEmptyFieldsState()
            } catch (e: IncorrectNumberException) {
                setIncorrectNumberState()
            }
        }
    }

    fun editCategory(newName: String, newLimitAmountString: String, id: Long) {

        viewModelScope.launch {

            try {
                checkEmptyFields(newName, newLimitAmountString)
                checkIncorrectNumbers(newLimitAmountString)

                val oldData = getCategoryUseCase(id).first()
                val oldLimit = getLimitByCategoryIdUseCase(id).first()
                val oldLimitAmount = oldLimit?.limitAmount ?: 0
                val newLimitAmount = newLimitAmountString.toInt()

                checkNoChanges(
                    listOf(newName, newLimitAmount),
                    listOf(oldData.name, oldLimitAmount)
                )

                val category = Category(
                    name = newName,
                    id = id
                )
                editCategoryUseCase(category)
                handleLimit(oldLimit, newLimitAmount, category)

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

    private suspend fun handleLimit(oldLimit: Limit?, newAmount: Int, category: Category) {

        val oldLimitAmount = oldLimit?.limitAmount ?: 0

        if (oldLimitAmount == 0 && newAmount != 0) {
            createLimit(newAmount, category.name, category.id)
        } else if (oldLimitAmount != 0 && newAmount == 0) {
            oldLimit?.let { removeLimitUseCase(it) }
        } else if (oldLimitAmount != newAmount) {
            oldLimit?.let { editLimit(it, newAmount) }
        }
    }

    private suspend fun createLimit(limitAmount: Int, categoryName: String, id: Long) {
        val limit = Limit(limitAmount, NO_DATA_VALUE, id, categoryName)
        addLimitUseCase(limit)
    }

    private suspend fun editLimit(limit: Limit, newAmount: Int) {
        val newLimit = limit.copy(limitAmount = newAmount)
        editLimitUseCase(newLimit)
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

    companion object {
        private const val NO_DATA_VALUE = 0
    }
}