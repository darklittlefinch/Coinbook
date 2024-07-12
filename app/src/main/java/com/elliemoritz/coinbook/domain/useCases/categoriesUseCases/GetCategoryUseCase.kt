package com.elliemoritz.coinbook.domain.useCases.categoriesUseCases

import com.elliemoritz.coinbook.domain.entities.ExpenseCategory
import com.elliemoritz.coinbook.domain.repositories.CategoriesRepository

class GetCategoryUseCase(
    private val categoriesRepository: CategoriesRepository
) {
    fun getCategory(id: Int): ExpenseCategory {
        return categoriesRepository.getCategory(id)
    }
}
