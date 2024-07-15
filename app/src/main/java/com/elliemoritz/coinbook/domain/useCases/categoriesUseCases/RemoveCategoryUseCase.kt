package com.elliemoritz.coinbook.domain.useCases.categoriesUseCases

import com.elliemoritz.coinbook.domain.entities.Category
import com.elliemoritz.coinbook.domain.repositories.CategoriesRepository

class RemoveCategoryUseCase(
    private val categoriesRepository: CategoriesRepository
) {
    fun removeCategory(category: Category) {
        categoriesRepository.removeCategory(category)
    }
}
