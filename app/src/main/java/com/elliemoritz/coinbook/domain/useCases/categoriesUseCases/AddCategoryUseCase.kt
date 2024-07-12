package com.elliemoritz.coinbook.domain.useCases.categoriesUseCases

import com.elliemoritz.coinbook.domain.entities.ExpenseCategory
import com.elliemoritz.coinbook.domain.repositories.CategoriesRepository

class AddCategoryUseCase(
    private val categoriesRepository: CategoriesRepository
) {
    fun addCategory(category: ExpenseCategory) {
        categoriesRepository.addCategory(category)
    }
}
