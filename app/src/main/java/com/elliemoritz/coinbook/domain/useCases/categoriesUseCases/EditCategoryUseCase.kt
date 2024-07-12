package com.elliemoritz.coinbook.domain.useCases.categoriesUseCases

import com.elliemoritz.coinbook.domain.entities.ExpenseCategory
import com.elliemoritz.coinbook.domain.repositories.CategoriesRepository

class EditCategoryUseCase(
    private val categoriesRepository: CategoriesRepository
) {
    fun editCategory(category: ExpenseCategory) {
        categoriesRepository.editCategory(category)
    }
}
