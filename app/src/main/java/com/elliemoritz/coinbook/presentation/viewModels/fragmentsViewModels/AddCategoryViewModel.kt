package com.elliemoritz.coinbook.presentation.viewModels.fragmentsViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elliemoritz.coinbook.domain.entities.Category
import com.elliemoritz.coinbook.domain.entities.Limit
import com.elliemoritz.coinbook.domain.useCases.categoriesUseCases.AddCategoryUseCase
import com.elliemoritz.coinbook.domain.useCases.categoriesUseCases.EditCategoryUseCase
import com.elliemoritz.coinbook.domain.useCases.categoriesUseCases.GetCategoryUseCase
import com.elliemoritz.coinbook.domain.useCases.limitsUseCases.AddLimitUseCase
import com.elliemoritz.coinbook.domain.useCases.limitsUseCases.EditLimitUseCase
import com.elliemoritz.coinbook.domain.useCases.limitsUseCases.GetLimitByCategoryIdUseCase
import com.elliemoritz.coinbook.domain.useCases.limitsUseCases.RemoveLimitUseCase
import com.elliemoritz.coinbook.presentation.states.fragmentsStates.FragmentCategoryState
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

    private val oldCategoryData = MutableSharedFlow<Category>()
    private val oldLimitData = MutableSharedFlow<Limit?>()

    private val nameDataFlow = oldCategoryData
        .map { FragmentCategoryState.NameData(it.name) }

    private val limitDataFlow = oldLimitData
        .map {
            val limitAmount = it?.amount ?: 0
            FragmentCategoryState.LimitData(limitAmount.toString())
        }

    private val errorFlow = MutableSharedFlow<FragmentCategoryState>()
    private val finishFlow = MutableSharedFlow<FragmentCategoryState>()

    private val _state = MutableSharedFlow<FragmentCategoryState>()
        .mergeWith(nameDataFlow)
        .mergeWith(limitDataFlow)
        .mergeWith(errorFlow)
        .mergeWith(finishFlow)

    val state: Flow<FragmentCategoryState>
        get() = _state

    fun setData(id: Int) {
        viewModelScope.launch {
            val categoryData = getCategoryUseCase(id).first()
            oldCategoryData.emit(categoryData)
            val limitData = getLimitByCategoryIdUseCase(categoryData.id).first()
            oldLimitData.emit(limitData)
        }
    }

    fun createCategory(name: String, limitString: String) {
        viewModelScope.launch {

            if (name.isEmpty() || limitString.isEmpty()) {
                setErrorState()
                return@launch
            }

            try {
                val limit = limitString.toInt()
                val category = Category(name)
                addCategoryUseCase(category)
                createLimit(limit, category.id)
                setFinishState()
            } catch (e: NumberFormatException) {
                setErrorState()
            }
        }
    }

    fun editCategory(id: Int, name: String, newLimitString: String) {
        viewModelScope.launch {

            if (name.isEmpty() || newLimitString.isEmpty()) {
                setErrorState()
                return@launch
            }

            try {
                val newLimit = newLimitString.toInt()
                val category = Category(name, id)
                editCategoryUseCase(category)

                val oldLimit = oldLimitData.first()
                handleLimit(oldLimit, newLimit, id)

                setFinishState()
            } catch (e: NumberFormatException) {
                setErrorState()
            }
        }
    }

    private suspend fun handleLimit(oldLimit: Limit?, newAmount: Int, categoryId: Int) {

        val oldLimitAmount = oldLimit?.amount ?: 0

        if (oldLimitAmount == 0 && newAmount != 0) {
            createLimit(newAmount, categoryId)
        } else if (oldLimitAmount != 0 && newAmount == 0) {
            oldLimit?.let { removeLimit(it) }
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

    private suspend fun removeLimit(limit: Limit) {
        removeLimitUseCase(limit)
    }

    private suspend fun setErrorState() {
        errorFlow.emit(FragmentCategoryState.Error)
    }

    private suspend fun setFinishState() {
        finishFlow.emit(FragmentCategoryState.Finish)
    }
}