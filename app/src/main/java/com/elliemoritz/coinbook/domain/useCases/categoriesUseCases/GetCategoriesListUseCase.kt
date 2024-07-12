package com.elliemoritz.coinbook.domain.useCases.categoriesUseCases

import androidx.lifecycle.LiveData
import com.elliemoritz.coinbook.domain.entities.ExpenseCategory
import com.elliemoritz.coinbook.domain.repositories.CategoriesRepository

class GetCategoriesListUseCase(
    private val categoriesRepository: CategoriesRepository
) {
    fun getCategoriesList(): LiveData<List<ExpenseCategory>> {
        return categoriesRepository.getCategoriesList()
    }
}
