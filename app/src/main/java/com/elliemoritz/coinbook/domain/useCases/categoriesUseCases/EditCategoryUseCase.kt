package com.elliemoritz.coinbook.domain.useCases.categoriesUseCases

import com.elliemoritz.coinbook.domain.entities.Category
import com.elliemoritz.coinbook.domain.repositories.CategoriesRepository

class EditCategoryUseCase(
    private val categoriesRepository: CategoriesRepository
) {
    suspend fun editCategory(category: Category) {
        categoriesRepository.editCategory(category)
    }
}
